import cats.Semigroup
import cats.data.{ NonEmptyList, OneAnd, Validated, ValidatedNel }
import cats.implicits._

def parseIntEither(s: String): Either[NumberFormatException, Int] =
  Either.catchOnly[NumberFormatException](s.toInt)

def parseIntValidated(s: String): ValidatedNel[NumberFormatException, Int] =
  Validated.catchOnly[NumberFormatException](s.toInt).toValidatedNel

List("1", "2", "3").traverse(parseIntValidated).isValid