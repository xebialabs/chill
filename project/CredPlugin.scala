import sbt.*
import sbt.Keys.credentials

object CredPlugin extends AutoPlugin {

  override def trigger = allRequirements

  override def buildSettings: Seq[Setting[_]] = {
    val maybeCredentials =
      (sys.env.get("NEXUS_HOST"), sys.env.get("NEXUS_USERNAME"), sys.env.get("NEXUS_PASSWORD")) match {
        case (Some(host), Some(user), Some(password)) =>
          Some(Credentials("Sonatype Nexus Repository Manager", host, user, password))
        case _ =>
          val credentialsFile = Path.userHome / ".sbt" / ".chill-credentials"
          if (credentialsFile.exists()) {
            Some(Credentials(credentialsFile))
          } else {
            None
          }
      }
    maybeCredentials.fold(Seq.empty[Def.Setting[_]])(c => Seq(credentials += c))
  }
}
