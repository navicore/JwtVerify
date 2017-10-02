package onextent.http.jtw.verify.routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{Directives, Route}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.typesafe.scalalogging.LazyLogging
import onextent.http.jtw.verify.ErrorSupport
import onextent.http.jtw.verify.models.JsonSupport
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim, JwtHeader, JwtOptions}

import scala.util.Try

object JwtRoute
    extends JsonSupport
    with LazyLogging
    with Directives
    with ErrorSupport {

  val cert = """
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1WV81wkxWlhV908Z77Te
5yn/Yq9lYDHaSnQeZ0iHxueR3C8Dpw0Fd+oEma11g/EsTpVqfHINUXODngW96Sbs
VTFvNUCcrjrDbFroWD8ehJy/leOVqDvqbLu9i7raUdWaXuP78wZULBOPXMhbwSM6
GaR7FliVcD9YdGItX7PrwiuplOMCWWud7A2gxfHczjCUz+7MMV620QjV08BJaWl2
05xP4Ym2vPDleAV2BVy92GqaTLBjZhbBCKKIYMHyy8F6DnV66IdcEGQh95NI2Kti
AvGnVltlBmmXA0DyYk5tPQSLThIYNr3sb/HVSZLvS3jWhHns0jRGVBoeAU1IQBff
uQIDAQAB
-----END PUBLIC KEY-----
    """.stripMargin

  def apply: Route =
    path(urlpath) {
      logRequest(urlpath) {
        handleErrors {
          cors(corsSettings) {
            get {
              logger.debug(s"get $urlpath")
              parameters('token.as[String]) { (token) =>

                val result: Try[(String, String, String)] = Jwt.decodeRawAll(token, cert, Seq(JwtAlgorithm.RS256))
                logger.info("ejs " + result.get._1)

                complete( HttpEntity(ContentTypes.`application/json`, s"$token") )
              }
            }
          }
        }
      }
    }
}
