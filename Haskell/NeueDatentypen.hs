--Aufgabe1----------------------------------------------------
newtype IN_0 = IN_0 Integer

instance Show IN_0 where
  show (IN_0 x)
    | x < 0 = "Nicht gueltig!"
    | otherwise = show (octoDar x)
        where octoDar :: Integer -> Integer                       --octoDar rechnet die gegebene IN_0 Zahl x
              octoDar x                                           --in Oktalzahldarstellung um
                | (x > 0) && ((mod x 8) /= 0) = 1+(octoDar(x-1))  
                | (x > 0) && ((mod x 8) == 0) = 3+(octoDar(x-1))   
                | otherwise = 0

--Aufgabe2----------------------------------------------------

data Antwort3 = Ja | Nein | Teilsteils deriving (Eq,Ord,Show)
type A3       = Antwort3

class Gueltig a where
 ist_gueltig :: a -> A3
 
instance Gueltig IN_0 where
  ist_gueltig (IN_0 x)       
   | x >= 0 = Ja
   | otherwise = Nein

--Aufgabe3----------------------------------------------------

instance Gueltig Int where
  ist_gueltig x = Ja
  
instance Gueltig Integer where
  ist_gueltig x = Ja

instance Gueltig Double where
  ist_gueltig x = Nein

instance Gueltig Float where
  ist_gueltig x
    | (x - realToFrac(truncate(x))) /= 0.0 = Nein
    | otherwise = Ja

--Aufgabe4----------------------------------------------------

instance Eq IN_0 where
 (IN_0 x) == (IN_0 y) 
   | (ist_gueltig(IN_0 x) == Ja) &&(ist_gueltig(IN_0 y) == Ja) = (x == y)
   | otherwise = False
 
instance Ord IN_0 where
  (IN_0 x) <= (IN_0 y)
    | (ist_gueltig(IN_0 x) == Ja) &&(ist_gueltig(IN_0 y) == Ja) = x <= y
    | otherwise = False

instance Enum IN_0 where
  toEnum x 
    | x >= 0 = (IN_0 (toInteger(x)))
    | otherwise = (IN_0 0)
  fromEnum (IN_0 x) = (fromEnum(x))
  succ x 
    | (ist_gueltig x) == Ja = (x+1)
    | otherwise = (IN_0 0)
  pred (IN_0 x) = (IN_0 (x-1))

instance Num IN_0 where
  (IN_0 x) + (IN_0 y) = (IN_0 (x+y))
  (IN_0 x) * (IN_0 y) = (IN_0 (x*y))
  negate (IN_0 x) = (IN_0 (-x))
  fromInteger x
    | x >= 0 = (IN_0 x)
    | otherwise = (IN_0 0)
  abs x 
   | ist_gueltig(x) == Nein = negate x
   | otherwise = x
  signum (IN_0 x)
    | x < 0 = (IN_0 0)
    | x == 0 = (IN_0 0)
    | otherwise = (IN_0 1)

--Aufgabe5----------------------------------------------------

newtype Nat_Liste = NL [IN_0] deriving (Eq,Show)

instance Gueltig Nat_Liste where
  ist_gueltig (NL []) = Ja
  ist_gueltig (NL [x]) = ist_gueltig(x)
  ist_gueltig (NL (x:xs)) 
    | ist_gueltig(x) == Ja = closerLook((NL xs),Ja)           --closerLook untersucht anhand des ersten Listenelements
    | ist_gueltig(x) == Nein = closerLook((NL xs),Nein)       --welcher der 3 Fälle für die Liste vorliegt
       where closerLook :: (Nat_Liste,Antwort3) -> Antwort3
             closerLook (NL [y],initial) 
               | ist_gueltig(y) /= initial = Teilsteils
               | otherwise = initial
             closerLook (NL (y:ys),initial)
               | ist_gueltig(y) == initial = closerLook((NL ys),initial)
               | ist_gueltig(y) /= initial = Teilsteils



