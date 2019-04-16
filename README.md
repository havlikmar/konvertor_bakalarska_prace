# konvertor_bakalarska_prace
Tento projekt je součástí bakalářské práce Aplikace na konverzi mezi různými způsoby reprezentace statistických dat ve formátu CSV, napsané Martinem Havlíkem v akademickém roce 2018/2019 na Vysoké škole ekonomické v Praze.</br>

<h2>Spuštění apikace</h2>
Sestavená aplikace se dá spustit z příkazové řádky ze složky „spustitelný archiv“. Pokud se spouští z ní, musí být všechny moduly, hlavní aplikace, soubory, které chceme transformovat, popř. další konfigurační soubory, jako slovníky metadat v jedné složce.</br>
</br>
Z důvodu konfigurace je potřeba zadat příkaz: „java -Dfile.encoding=UTF-8 -cp "*" com/github/havlikmar/konvertor_bakalarska_prace/main/App“. </br>
</br>
Pro usnadnění spouštění byl přidán do složky „spustitelný archiv“ soubor execute.bat, který umožňuje spuštění aplikace jedním klikem.</br>

<h2>Práce s aplikací</h2>
Aplikace se ovládá z příkazové řádky. V této části jsou popsány jednotlivé kroky zadávání příkazů. V případě jakýchkoliv problémů se aplikace ukončí pomocí příkazu „stop application“. 
Na výpisu č. 1, lze vidět postup výběru formátů.</br>

Výpis 1: Zadávání výběru formátů. Zdroj (Autor)</br>
1 Vyberte vstupní formát. Podporované formáty jsou: CsvmDen CsvDen CsvmNor CsvNor</br>
2 CsvmDen</br>
3 Vyberte výstupní formát. Podporované formáty jsou: CsvmDen CsvDen CsvmNor CsvNor</br>
4 CsvDen</br>

Při zadávání příkazů se opíše požadovaný název formátu ze seznamu. Výběr hlavního souboru lze vidět na výpisu č. 2.</br>

Výpis 2: Zadávání názvu hlavního souboru. Zdroj (Autor)</br>
1 Zadejte název hlavního vstupního souboru s příponou.</br>
2 csvm.csv</br>
3 Zadejte separátor.</br>
4 ,</br>

Při zadávání hlavního souboru se zadává jeho název, včetně přípony. Následně se uvede také jednoznakový separátor. Výběr dodatečných souborů u normalizovaného způsobu reprezentace lze vidět na výpisu č. 3. </br>

Výpis 3: Zadávání názvů dimensionálních souborů. Zdroj (Autor)</br>
1 Zadejte název vedlejšího (dimensionálního) vstupního souboru s příponou.</br>
2 csvm1.csv</br>
3 Zadejte separátor.</br>
4 ,</br>
5 Přejete si přidat další vedlejší formát. [Y/N]</br>
6 N</br>

Výběr dimensionálních souborů probíhá obdobně jako u hlavního souboru. Zadává se zde název souboru a také separátor. Následně je potřeba potvrdit, zda uživatel chce přidat další soubor. Zadává se to znaky „Y“ (chce přidat další soubor), nebo „N“ (nechce přidat další soubor). Stejný proces potvrzování a zamítání je použit v celé aplikaci. Proces zadávání příkazu při denormalizaci lze vidět na výpisu č. 4.</br>

Výpis 4: Zadávání příkazů při denormalizaci. Zdroj (Autor)</br>
1 Napište sloupec z hlavního souboru a vedlejšího, mezi kterými chcete vytvořit spojení pro sjednocení do jednoho souboru.</br>
2</br>
3 Formát je ve tvaru: názevSloupceHlavníSoubor názevSloupceVedlejsíSoubor</br>
4 Výpis sloupců z hlavního souboru: ID, MODEL, TYPE, ID_MANUFACTURE</br>
5 Výpis sloupců z vedlejšího (dimenzionálního) souboru: ID, MANUFACTURER</br>
6 ID_MANUFACTURE ID</br>

Při denormalizaci se zadává název sloupce z tabulky faktů a za ním název sloupce dimenzionální tabulky, viz výpis č. 10. Názvy sloupců z obou tabulek jsou vypsané (viz výpis č. 4). Proces zadávání příkazů při normalizaci lze vidět na výpisu č. 5.</br>

Výpis 5: Zadávání příkazů při normalizaci. Zdroj (Autor)</br>
1  Napište sloupce z hlavního souboru, z kterých chcete vytvořit novou dimensionální tabulku.</br>
2  Formát je ve tvaru: názevSloupce názevSloupce ... ...</br>
3</br>
4  Výpis sloupců z hlavního souboru: idhod, hodnota, stapro_kod, reprcen_cis, reprcen_kod,</br>
5  obdobiod, obdobido, uzemi_cis, uzemi_kod, uzemi_txt, reprcen_txt</br>
6 </br>
7  uzemi_cis uzemi_kod uzemi_txt</br>
8  Zadejte jednoslovný unikátní název sloupce, který bude sloužit jako primární/cizí klíč</br>
9  id_zeme</br>
10 Zadejte jednoslovný unikátní název nového souboru, bez přípony</br>
11 zeme</br>

Při normalizaci se zadávají jednotlivé názvy sloupců z vypsaného seznamu. Tyto sloupce se pak přidají do číselníku. Následně se zvolí název primárního klíče. Jde o jednoslovní název. Poslední krok je výběr názvu souboru. Zde se vybírá název bez přípony. Poslední výpis č. 6 představuje proces zadávání metadat pro novou dimenzionální tabulku u CSVM formátu.</br>

Výpis 6: Zadávání příkazů při přidání metadat. Zdroj (Autor)</br>
1 Přejete si přidat metadata k primárnímu klíči. [Y/N]</br>
2 Y</br>
3 Zadejte popis nového souboru</br>
4 popis modelů aut</br>
5 Zadejte maximální počet cifer primárního klíče</br>
6 50</br>

Přidání metadat je volitelné. Jejich výběr se potvrdí nebo zamítne pomocí znaku „Y“ nebo „N“. Následně se zadá popis souboru. Ten může obsahovat více slov. Nakonec se zadá počet cifer primárního klíče. Tato hodnota, není nikde ověřována a slouží pouze jako metadata.</br>  
