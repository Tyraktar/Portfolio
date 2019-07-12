type Nat0 = Int

data Zeichenvorrat = A | B | C | D | E | F | G | H | I | J | K deriving (Eq,Show)
data Bandalphabet  = BA Zeichenvorrat | Leer deriving (Eq,Show)

type Halbband         = [Bandalphabet]
type Linkes_Halbband  = Halbband
type Rechtes_Halbband = Halbband
type Rechenband       = (Linkes_Halbband,Rechtes_Halbband)

type Index                          = Int
type Lese_Schreibkopf_Position      = Index
type Zeichen_unter_Lese_Schreibkopf = Bandalphabet
data Richtung                       = Links | Rechts deriving (Eq,Show)
data Befehl                         = Drucke Bandalphabet
                                       | Bewege_Lese_Schreibkopf_nach Richtung deriving (Eq,Show)

type LSk_Position = Lese_Schreibkopf_Position
type LSk_Zeichen  = Zeichen_unter_Lese_Schreibkopf

type Zustand               = Nat0
type Interner_Zustand      = Zustand
type Interner_Folgezustand = Zustand

type Zeile       = (Interner_Zustand,LSk_Zeichen,Befehl,Interner_Folgezustand)
type Turingtafel = [Zeile]

data Turingmaschinenzustand = TMZ Turingtafel Interner_Zustand Rechenband LSk_Position

type Initiales_Rechenband = Rechenband
data Sim_Eingabe          = SE Turingtafel Initiales_Rechenband

type Finaler_interner_Zustand         = Zustand
type Finale_Lese_Schreibkopf_Position = Index
type Finales_Rechenband               = Rechenband

type Finaler_iZ  = Finaler_interner_Zustand
type Finale_LSkP = Finale_Lese_Schreibkopf_Position
type Finales_Rb  = Finales_Rechenband

data Sim_Ausgabe = SA Finaler_iZ Finales_Rb Finale_LSkP

ist_zulaessige_Turingtafel :: Turingtafel -> Bool
ist_zulaessige_Turingtafel [] = False
ist_zulaessige_Turingtafel ((iz,z,b,ifz):xs)
  | xs /= [] = (foo (iz,z,b,ifz) xs) && ist_zulaessige_Turingtafel xs
  | otherwise = True
    where foo :: Zeile -> Turingtafel -> Bool
          foo (iz,z,b,ifz) ((iz',z',b',ifz'):xs)
           | xs /= [] = (iz /= iz') || (z /= z')  && foo (iz,z,b,ifz) xs
           | otherwise = (iz /= iz') || (z /= z')

ist_zulaessiges_initiales_Rechenband :: Rechenband -> Bool
ist_zulaessiges_initiales_Rechenband (lB,rB) = (length lB) == 0

tm_transition :: Turingmaschinenzustand -> Turingmaschinenzustand
tm_transition (TMZ t izm (lB,[]) lSkP) = tm_transition(TMZ t izm (lB,[Leer]) lSkP)
tm_transition (TMZ t izm (lB,rB) lSkP)
  | (exestiert_Zeile t izm (if(lSkP < 0) then lB!!(abs(lSkP)-1) else rB!!lSkP)) = (berechne_Zustand t (suche_Zeile t izm (if(lSkP < 0) then lB!!(abs(lSkP)) else rB!!lSkP)) (lB,rB) lSkP)
  | otherwise = (TMZ t izm (lB,rB) lSkP)


berechne_Zustand :: Turingtafel -> Zeile -> Rechenband -> LSk_Position -> Turingmaschinenzustand
berechne_Zustand t (iz,z,b,ifz) (lB,rB) lSkP 
  | b == Bewege_Lese_Schreibkopf_nach Links = (TMZ t ifz (if ((lSkP-1) < 0 && abs(lSkP-1)-1 > length(lB)) then (lB++[Leer]) else lB,rB) (lSkP-1))
  | b == Bewege_Lese_Schreibkopf_nach Rechts = (TMZ t ifz (lB,if (lSkP+1) < length(rB) then rB else (rB++[Leer])) (lSkP+1)) 
  | otherwise = (TMZ t ifz (if(lSkP < 0) then (ersetze_Zeichen lB b (abs(lSkP)-1)) else lB,if(lSkP >= 0) then (ersetze_Zeichen rB b lSkP)else rB) lSkP) 

exestiert_Zeile :: Turingtafel -> Zustand -> Bandalphabet -> Bool
exestiert_Zeile [] z c = False
exestiert_Zeile ((iz,tc,b,ifz):ts) z c
  | iz == z && c == tc = True
  | otherwise = exestiert_Zeile ts z c

suche_Zeile :: Turingtafel -> Zustand -> Bandalphabet -> Zeile
suche_Zeile ((iz,n,b,ifz):ts) z c
  | iz == z && n == c = (iz,n,b,ifz)
  | otherwise = suche_Zeile ts z c

ersetze_Zeichen :: Halbband -> Befehl -> Nat0 -> Halbband
ersetze_Zeichen (r:rs) (Drucke z) i
  | i > 0 = [r]++(ersetze_Zeichen rs (Drucke z) (i-1))
  | otherwise = [z]++rs

type Spur = [Turingmaschinenzustand]
tm_spur :: Turingmaschinenzustand -> Spur
tm_spur (TMZ t izm (lB,rB) lSkP)
  | not(ist_gleicher_TMZ (TMZ t izm (lB,rB) lSkP) (tm_transition(TMZ t izm (lB,rB) lSkP))) = [tm_transition (TMZ t izm (lB,rB) lSkP)]++tm_spur(tm_transition(TMZ t izm (lB,rB) lSkP))
  | otherwise = []

ist_gleicher_TMZ :: Turingmaschinenzustand -> Turingmaschinenzustand -> Bool
ist_gleicher_TMZ (TMZ t1 z1 (lB1,rB1) lSkP1) (TMZ t2 z2 (lB2,rB2) lSkP2)
  | (z1 == z2 && lB1 == lB2 && rB1 == rB2 && lSkP1 == lSkP2) = True
  | otherwise = False  
 
zeige_zustand :: Turingmaschinenzustand -> String
zeige_zustand (TMZ t iz (lB,rB) lSkP) = "("++show(iz)++","++(bilde_Band lB rB)++","++show(lSkP)++")"

bilde_Band :: Halbband -> Halbband -> String
bilde_Band lB (rB:rBs) = "["++(if(loese_Band(reverse(lB)) /= []) then loese_Band(reverse(lB))++"," else "")++"0:"++zeige_Zeichen(rB)++(if(loese_Band(rBs) /= []) then ","++loese_Band(rBs) else "")++"]"

loese_Band :: Halbband -> String
loese_Band [] = []
loese_Band (b:bs)
  | bs /= [] = zeige_Zeichen(b)++","++loese_Band(bs)
  | otherwise = zeige_Zeichen(b)

zeige_Zeichen :: Bandalphabet -> String
zeige_Zeichen (BA b) = show(b)
zeige_Zeichen Leer = show(Leer)

zeige_spur :: Spur -> String
zeige_spur [] = []
zeige_spur [x] = zeige_zustand x
zeige_spur (x:xs) = (zeige_zustand x)++" ->> "++zeige_spur xs

tm_sim :: Sim_Eingabe -> Sim_Ausgabe
tm_sim (SE t iR)
  | ist_zulaessige_Turingtafel(t) && ist_zulaessiges_initiales_Rechenband(iR) = bilde_Ausgabe((tm_spur(TMZ t 0 iR 0)))

bilde_Ausgabe :: Spur -> Sim_Ausgabe
bilde_Ausgabe [(TMZ t z rB lSkP)] = (SA z rB lSkP)
bilde_Ausgabe (s:ss) = bilde_Ausgabe ss

  
instance Show Sim_Ausgabe where
 show (SA iz (lB,rB) lSkP) = "iZ: "++show(iz)++"; Band: "++(bilde_Ausgabeband lB rB)++"; LS-Pos: "++show(lSkP)

bilde_Ausgabeband :: Halbband -> Halbband -> String
bilde_Ausgabeband lB (rB:rBs) = "["++(if(ohne_Leer(lB) /= []) then loese_Band(reverse(ohne_Leer(lB)))++"," else "")
                                 ++"0:"++zeige_Zeichen(rB)
                                 ++(if(ohne_Leer(reverse(rBs)) /= []) then ","++loese_Band(reverse(ohne_Leer(reverse(rBs)))) else "")++"]"

ohne_Leer :: Halbband -> Halbband
ohne_Leer [] = []
ohne_Leer (hB:hBs)
 | hB /= Leer = hB:hBs
 | otherwise = ohne_Leer hBs
 

