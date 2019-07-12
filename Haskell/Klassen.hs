--Aufgabe1--------------------------------------------------------------

newtype IN_0 = IN_0 Integer deriving (Eq,Ord,Show)

data Antwort3 = Ja | Nein | Teilsteils deriving (Eq,Ord,Show)
type A3       = Antwort3

class Gueltig a where
 ist_gueltig :: a -> A3
 
instance Gueltig IN_0 where
  ist_gueltig (IN_0 x)       
   | x >= 0 = Ja
   | otherwise = Nein

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

class (Eq a,Ord a, Show a, Num a) => FFGB a where
  fac      :: a -> a
  fib      :: a -> a
  ggt      :: a -> a -> a
  binom    :: a -> a -> a
  div_ffgb :: a -> a -> a
  
  fac 0 = 1
  fac n = n*fac (n-1) where otherwise = True 
  fib 0 = 0
  fib 1 = 1
  fib n = fib(n-1)+fib(n-2)
  ggt m n
   | m == n = n
   | m > n = ggt(m-n) n
   | m < n = ggt m (n-m)
  binom n k = div_ffgb (fac n) ((fac k) * (fac (n-k)))

instance FFGB Int where
  div_ffgb x y = div x y
  
instance FFGB Integer where
  div_ffgb x y = div x y
  fac 0 = 1
  fac n
   | n < 0 = -1
   | otherwise = n*fac (n-1) where otherwise = True
  fib 0 = 0
  fib 1 = 1   
  fib n 
   | n < 0 = -1
   | otherwise = fib(n-1)+fib(n-2)
  ggt m n 
   | (m <= 0) || (n <= 0) = -1
   | m == n = n
   | m > n = ggt(m-n) n
   | m < n = ggt m (n-m)
  binom n k 
   | n < 0 || k < 0 = -1
   | otherwise = div_ffgb (fac n) ((fac k) * (fac (n-k)))

instance FFGB IN_0 where
  div_ffgb (IN_0 x) (IN_0 y)
   | (ist_gueltig(IN_0 x) == Nein) || (ist_gueltig(IN_0 y) == Nein) || ((IN_0 y) == (IN_0 0)) = (IN_0 (-1))
   | otherwise = (IN_0 (div x y))
  fac (IN_0 n) 
   | ist_gueltig(IN_0 n) == Ja = (IN_0 (fac n))
   | otherwise = (IN_0 n)
  fib (IN_0 n)
   | ist_gueltig(IN_0 n) == Ja = (IN_0 (fib n))
   | otherwise = (IN_0 n)
  ggt (IN_0 m) (IN_0 n)
   | (ist_gueltig(IN_0 m) == Ja) && (ist_gueltig(IN_0 n) == Ja) && (m > 0) && (n > 0) = (IN_0 (ggt m n))
   | (ist_gueltig(IN_0 m) == Nein) || (m == 0) = (IN_0 m)
   | otherwise = (IN_0 n)
  binom (IN_0 n) (IN_0 k)
   | (ist_gueltig(IN_0 n) == Ja)&&(ist_gueltig(IN_0 k) == Ja)&&(k <= n) = (IN_0 (binom n k))
   | otherwise = (IN_0 (-99))

--Aufgabe2--------------------------------------------------------------

type Matrioid     = [[Integer]]
type Typ          = (IN_0,IN_0)
data Matrix       = Matrix Typ Matrioid deriving (Eq,Show)
data Determinante = Determinante_ist Integer
                     | Determinante_ist_undefiniert deriving (Eq,Show)

m = Matrix (IN_0 3,IN_0 5) [[]]
class Quadratisch a where
 ist_quadratisch :: a -> Bool

instance Quadratisch Matrix where
 ist_quadratisch (Matrix (IN_0 n,IN_0 m) x)
  | (ist_gueltig(IN_0 n) == Ja) && (n >= 1) && (n == m) = True
  | otherwise = False  
  
--Aufgabe3--------------------------------------------------------------

determinante :: Matrix -> Determinante
determinante x
 | ist_quadratisch(x) == True = berechnePos x 0 0 (length x)
 | otherwise = Determinante_ist_undefiniert
   where berechnePos :: Matrix -> Int -> Int -> Int -> Determinante
         berechnePos (Matrix (IN_0 m,IN_0 n) mat) i j l
          | l > 0 = ((mat!!(mod i m)!!(mod j m))*berechnePos(
                    +berechnePos (Matrix (IN_0 m,IN_0 n) mat) i (j+1) (l-1)
          | otherwise = 0
         berechneNeg :: Matrix -> Int -> Int -> Int -> Determinante
         berechneNeg (Matrix (IN_0 m,IN_0 n) mat) i j l
          | l > 0 = (mat!!(mod i m)!!(mod j m))-berechneNeg (Matrix (IN_0 m,IN_0 n) mat) i (j+1) (l-1)
          | otherwise = 0

    
--Aufgabe4--------------------------------------------------------------

newtype Zeichenreihe = Z String deriving (Eq,Show)
newtype Tripel       = T (IN_0, Zeichenreihe,Matrix) deriving (Eq,Show)
newtype Liste        = L [Tripel] deriving (Eq,Show)

instance Quadratisch IN_0 where
 ist_quadratisch (IN_0 x)
  | (ist_gueltig(IN_0 x) == Ja)&&(((floor(sqrt(fromIntegral(x))))^2) == x) = True
  | otherwise = False  

instance Quadratisch Zeichenreihe where                                           --Quadratische Strings sind echt Teilbar durch ihre
 ist_quadratisch (Z s) = konkatisierbar s (findeTeiler (length s) 1) where        --sich wiederholenden Teilstücke
   konkatisierbar :: String->[Int]->Bool                                          --Daher überprüfe ich die Menge an Teilstrings der Länge dieser Teiler
   konkatisierbar _ [] = False                                                    --um zu zeigen ob ein String quadratisch ist oder nicht
   konkatisierbar s (y:ys)
    | s == repeatString (buildTeilString s y) (div (length s) y) = True
    | otherwise = konkatisierbar s (ys)

repeatString :: String->Int->String                                   --Wiederholt einen gegebenen String i-mal und gibt diesen zurück
repeatString s i 
 | i /= 0 = s++repeatString s (i-1)
 | otherwise = []
 
buildTeilString :: String->Int->String                                --Gibt einen Teilstring der Länge i zurück, beginnend vom ersten Element
buildTeilString (x:xs) i
 | i >= 1 = [x]++buildTeilString xs (i-1)
 | otherwise = []

findeTeiler :: Int -> Int -> [Int]                                    --Findet alle Teiler einer Zahl und speichert sie in eine Liste
findeTeiler x i
  | ((div x 2) >= i) && ((mod x i) == 0) = [i]++findeTeiler x (i+1)
  | ((div x 2) >= i) && ((mod x i) /= 0) = findeTeiler x (i+1)
  | otherwise = []

instance Quadratisch Tripel where
 ist_quadratisch (T (x,y,z)) = (ist_quadratisch(x)&&ist_quadratisch(y)&&ist_quadratisch(z))  --Laut Angabe

instance Quadratisch Liste where                                      --Überprüft für jedes Element in der Liste ob es quadratisch ist
 ist_quadratisch (L []) = False
 ist_quadratisch (L [x]) = ist_quadratisch x
 ist_quadratisch (L (x:xs))
  | ist_quadratisch x == True = ist_quadratisch(L xs)
  | otherwise = False
 