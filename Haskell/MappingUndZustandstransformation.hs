--Aufgabe1----------------------------------------------------------
map_2 :: (a -> b) -> (a -> b) -> [a] -> [b]
map_2 _ _ [] = []
map_2 f g (x:[]) = f x : []
map_2 f g (x:y:xs) = f x : g y : map_2 f g xs 

--Aufgabe2----------------------------------------------------------
map_n :: [(a -> b)] -> [a] -> [b]
map_n _ [] = []
map_n [] _ = []
map_n (f:fs) (x:xs)
  | length (f:fs) >= length (x:xs) = f x : map_n fs xs      --Für den Fall, dass wir mehr Funktionen als Argumente haben
  | otherwise = zyklus (f:fs) (x:xs) 0                      --wird die Liste einfach durchgegangen, sonst werden die Funktionen
                                                            --mithilfe von zyklus zyklisch aufgerufen
zyklus :: [(a->b)] -> [a] -> Int -> [b]
zyklus _ [] _ = []
zyklus fl (x:xs) i
  | length fl /= i = (fl!!i) x : zyklus fl xs (i+1)
  | otherwise = zyklus fl (x:xs) 0

--Aufgabe3----------------------------------------------------------
type Nat1 = Integer

ggt_euklid :: Nat1 -> (Nat1 -> Nat1)
ggt_euklid x y
  | x == y = x
  | x > y = ggt_euklid (x-y) y
  | x < y = ggt_euklid x (y-x) 

data Variable              = X | Y deriving (Eq,Ord,Enum,Show)
type Variablen             = Variable
type Zustand               = (Variablen -> Nat1)
type Zustandsmenge         = Zustand
type Sigma                 = Zustandsmenge
type Zustandstransformator = (Sigma -> Sigma)
 
wellenpfeil :: Zustandstransformator                                 --Funktioniert wie Euklids Verfahren
wellenpfeil f(x)                                                     --Der aktuelle Wert von X oder Y bestimmt das weitere Aussehen der Funktion die den Zustand beschreibt
  |f(X) < f(Y) = if x==X then f(X) else (f(Y)-f(X))
  |f(X) > f(Y) = if x==X then (f(X)-f(Y)) else f(Y)
  |f(X) == f(Y) = f(x)

--Aufgabe4----------------------------------------------------------
ggt :: Zustandstransformator
ggt f = if f(X) == f(Y) then f else ggt(wellenpfeil(f))             --Wellenpfeil ist hier der angewandte Algorithmus, 
                                                                    --die Funktion ggt führt diese solange aus bis der GGT erreicht ist
--Aufgabe5----------------------------------------------------------
type Spur = [Sigma]

ggt_spur :: Sigma -> Spur                                              --Wie ggt, nur wird bei jedem Aufruf die aktuelle beschreibende Funktion "davorgehängt"
ggt_spur f = if f(X) == f(Y) then [f] else f:ggt_spur(wellenpfeil(f))

--Aufgabe6----------------------------------------------------------
zeige_spur :: Spur -> String                                           --Behandelt Fälle von leeren Listen und fügt [] an eine Spur hinzu
zeige_spur [] = []
zeige_spur si = "["++zeige_spurSpur(si)++"]"

zeige_spurSpur :: Spur -> String                                        --Berrechnet die X und Y für jede Spur in der Liste und fügt diese mit dem gewünschten String zusammen
zeige_spurSpur [si] = "(x<-"++(show(si(X)))++",y<-"++(show(si(Y)))++")"     
zeige_spurSpur (si:sis) = "(x<-"++(show(si(X)))++",y<-"++(show(si(Y)))++")"++" -w-> "++zeige_spurSpur(sis)

--Aufgabe7----------------------------------------------------------
newtype Spur' = Sp Spur

instance Show Spur' where
  show (Sp ([])) = ""
  show (Sp ([si])) = "(x<-"++(show(si(X)))++",y<-"++(show(si(Y)))++")"    --Wie zeige_spur nur wird hier der SP Typ erkannt, womit Spuren direkt ausgelesen werden können
  show (Sp (si:sis)) = "(x<-"++(show(si(X)))++",y<-"++(show(si(Y)))++")"++" -w-> "++show(Sp sis)