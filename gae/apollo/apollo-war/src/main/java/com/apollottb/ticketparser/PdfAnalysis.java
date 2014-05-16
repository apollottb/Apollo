package com.apollottb.ticketparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfAnalysis
{
	public PdfContent pdfContent;
	public ArrayList<TripDraft> trips;
	
	public ArrayList<PdfWord> airlines;
	public ArrayList<PdfWord> dates;
	public ArrayList<PdfWord> times;
	public ArrayList<PdfWord> locations;
	
	public static final int INTERPAGE_MARGIN = 30;
	
	private String regexDate;
	private String regexTime;
	private String regexFlight;
	private static ArrayList<Float> adjustedHeights;
	private ArrayList<Airport> airports;
	
	
	public PdfAnalysis(InputStream airportsFileStream)
	{
		trips = new ArrayList<TripDraft>();
		try
		{
			AirportsFileParser airportsFileParser = new AirportsFileParser(airportsFileStream);
			airports = airportsFileParser.airports;
		}
		catch (IOException e)
		{
			System.out.println("Failed to open airports.dat.");
			e.printStackTrace();
		}
		
		
		regexDate = "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|January|February|March|April|May|June|July|August|September|October|November|December)( |-|/)(3[01]|[12][0-9]|0?[1-9])";
		regexDate = "(" + regexDate + ")|(" + "(1[012]|0?[1-9])(-|/)(3[01]|[12][0-9]|0?[1-9])" + ") ";
		regexTime = "(0?[0-9]|1[0-2]):[0-5][0-9] ?(PM|AM|A|P)";
		regexTime = "(" + regexTime + ")|(" + "(0?[0-9]|1[0-9]|2[0-3]):[0-5][0-9]" + ")";
		regexFlight = " ((1Time Airline)|(1T)|(RNX)|(NEXTIME)|(Ansett Australia)|(AN)|(AAA)|(ANSETT)|(Abacus International)|(1B)|(Aigle Azur)|(ZI)|(AAF)|(AIGLE AZUR)|(Aloha Airlines)|(AQ)|(AAH)|(ALOHA)|(American Airlines)|(AA)|(AAL)|(AMERICAN)|(Asiana Airlines)|(OZ)|(AAR)|(ASIANA)|(Afriqiyah Airways)|(8U)|(AAW)|(AFRIQIYAH)|(Allegiant Air)|(G4)|(AAY)|(ALLEGIANT)|(Aban Air)|(K5)|(ABE)|(ABAN)|(Astral Aviation)|(8V)|(ACP)|(ASTRAL CARGO)|(Air Tindi)|(8T)|(Ada Air)|(ZY)|(ADE)|(ADA AIR)|(Adria Airways)|(JP)|(ADR)|(ADRIA)|(Air Europa)|(UX)|(AEA)|(EUROPA)|(Aero Benin)|(EM)|(AEB)|(AEROBEN)|(Aegean Airlines)|(A3)|(AEE)|(AEGEAN)|(Air Europe)|(PE)|(AEL)|(AIR EUROPE)|(Alaska Central Express)|(KO)|(AER)|(ACE AIR)|(ACES Colombia)|(AES)|(ACES)|(Astraeus)|(5W)|(AEU)|(FLYSTAR)|(Aerosvit Airlines)|(VV)|(AEW)|(AEROSVIT)|(Air Italy)|(I9)|(AEY)|(AIR ITALY)|(Alliance Airlines)|(QQ)|(UTY)|(UNITY)|(Ariana Afghan Airlines)|(FG)|(AFG)|(ARIANA)|(Aeroflot Russian Airlines)|(SU)|(AFL)|(AEROFLOT)|(Air Bosna)|(JA)|(BON)|(AIR BOSNA)|(Air France)|(AF)|(AFR)|(AIRFRANS)|(Air Caledonie International)|(SB)|(ACI)|(AIRCALIN)|(Air Salone)|(2O)|(Air Cargo Carriers)|(2Q)|(SNC)|(NIGHT CARGO)|(Air Namibia)|(SW)|(NMB)|(NAMIBIA)|(Aerolitoral)|(5D)|(SLI)|(COSTERA)|(Air Glaciers)|(7T)|(AGV)|(AIR GLACIERS)|(Aviogenex)|(AGX)|(GENEX)|(Aeroper)|(PL)|(PLI)|(Aeroperu)|(Atlas Blue)|(8A)|(BMM)|(ATLAS BLUE)|(Azerbaijan Airlines)|(J2)|(AHY)|(AZAL)|(Avies)|(U3)|(AIA)|(AVIES)|(Airblue)|(ED)|(ABQ)|(PAKBLUE)|(Airlift International)|(AIR)|(AIRLIFT)|(Air Berlin)|(AB)|(BER)|(AIR BERLIN)|(Air India Limited)|(AI)|(AIC)|(AIRINDIA)|(Air Bourbon)|(ZB)|(BUB)|(BOURBON)|(Air Atlanta Icelandic)|(CC)|(ABD)|(ATLANTA)|(Air Tahiti Nui)|(TN)|(THT)|(TAHITI AIRLINES)|(Arkia Israel Airlines)|(IZ)|(AIZ)|(ARKIA)|(Air Jamaica)|(JM)|(AJM)|(JAMAICA)|(Air One)|(AP)|(ADH)|(HERON)|(Air Sahara)|(S2)|(RSH)|(SAHARA)|(Air Malta)|(KM)|(AMC)|(AIR MALTA)|(Air Japan)|(NQ)|(AJX)|(AIR JAPAN)|(Air Kiribati)|(4A)|(AKL)|(America West Airlines)|(HP)|(AWE)|(CACTUS)|(Air Wisconsin)|(ZW)|(AWI)|(AIR WISCONSIN)|(Tatarstan Airlines)|(U9)|(TAK)|(TATARSTAN)|(Allegheny Commuter Airlines)|(ALO)|(ALLEGHENY)|(Air Sunshine)|(RSI)|(AIR SUNSHINE)|(Air Libert)|(VD)|(Air Malawi)|(QM)|(AML)|(MALAWI)|(Air Sicilia)|(BM)|(ATA Airlines)|(AMT)|(AMTRAN)|(Air Macau)|(NX)|(AMU)|(AIR MACAO)|(AMC Airlines)|(AMV)|(Air Seychelles)|(HM)|(SEY)|(SEYCHELLES)|(All Nippon Airways)|(ANA All Nippon Airways)|(NH)|(ANA)|(ALL NIPPON)|(Air Nostrum)|(YW)|(ANE)|(AIR NOSTRUM)|(Air Niugini)|(PX)|(ANG)|(NUIGINI)|(Air Arabia)|(G9)|(ABY)|(ARABIA)|(Air Canada)|(AC)|(ACA)|(AIR CANADA)|(Air Baltic)|(BT)|(BTI)|(AIRBALTIC)|(Air Nippon)|(EL)|(ANK)|(ANK AIR)|(Airnorth)|(TL)|(ANO)|(TOPEND)|(Air New Zealand)|(NZ)|(ANZ)|(NEW ZEALAND)|(Alitalia Express)|(XM)|(SMX)|(ALIEXPRESS)|(Aero Flight)|(GV)|(ARF)|(Aero Fox)|(Arrow Air)|(JW)|(APW)|(BIG A)|(Aerocondor)|(2B)|(ARD)|(AEROCONDOR)|(Aerolineas Argentinas)|(AR)|(ARG)|(ARGENTINA)|(Air Sinai)|(4D)|(ASD)|(AIR SINAI)|(Atlantic Southeast Airlines)|(EV)|(ASQ)|(ACEY)|(Astrakhan Airlines)|(OB)|(ASZ)|(AIR ASTRAKHAN)|(Air Tanzania)|(TC)|(ATC)|(TANZANIA)|(Air Burkina)|(2J)|(VBW)|(BURKINA)|(Airlines Of Tasmania)|(FO)|(ATM)|(AIRTAS)|(Air Saint Pierre)|(PJ)|(SPM)|(Austrian Airlines)|(OS)|(AUA)|(AUSTRIAN)|(Air Southwest)|(WOW)|(SWALLOW)|(ATUR)|(TUR)|(Abu Dhabi Amiri Flight)|(MO)|(AUH)|(SULTAN)|(Aurigny Air Services)|(GR)|(AUR)|(AYLINE)|(Austral Lineas Aereas)|(AU)|(AUT)|(AUSTRAL)|(Air Vanuatu)|(NF)|(AVN)|(AIR VAN)|(Air Bangladesh)|(B9)|(BGD)|(AIR BANGLA)|(Air Mediterranee)|(DR)|(BIE)|(MEDITERRANEE)|(Air Moorea)|(TAH)|(AIR MOOREA)|(Air Wales)|(6G)|(AWW)|(RED DRAGON)|(Air India Express)|(IX)|(AXB)|(EXPRESS INDIA)|(Air Exel)|(XT)|(AXL)|(EXEL COMMUTER)|(AirAsia)|(Air Asia)|(AK)|(AXM)|(ASIAN EXPRESS)|(Alitalia)|(AZ)|(AZA)|(ALITALIA)|(Amaszonas)|(Z8)|(AZN)|(Air Zimbabwe)|(UM)|(AZW)|(AIR ZIMBABWE)|(Aserca Airlines)|(R7)|(OCA)|(AROSCA)|(American Eagle Airlines)|(MQ)|(EGF)|(EAGLE FLIGHT)|(AD Aviation)|(VUE)|(FLIGHTVUE)|(Air Ivoire)|(VU)|(VUN)|(AIRIVOIRE)|(Air Botswana)|(BP)|(BOT)|(BOTSWANA)|(Air Foyle)|(GS)|(UPA)|(FOYLE)|(Air Tahiti)|(VT)|(VTA)|(AIR TAHITI)|(Air VIA)|(VL)|(VIM)|(Africa West)|(FK)|(WTA)|(WEST TOGO)|(ATRAN Cargo Airlines)|(V8)|(VAS)|(ATRAN)|(Air China)|(CA)|(CCA)|(AIR CHINA)|(Air Chathams)|(CV)|(CVA)|(CHATHAM)|(Air Marshall Islands)|(CW)|(CWM)|(AIR MARSHALLS)|(Access Air)|(ZA)|(CYD)|(CYCLONE)|(Air Algerie)|(AH)|(DAH)|(AIR ALGERIE)|(Adam Air)|(KI)|(DHI)|(ADAM SKY)|(Air Dolomiti)|(EN)|(DLA)|(DOLOMOTI)|(Air Madrid)|(NM)|(DRD)|(ALADA AIR)|(Aer Lingus)|(EI)|(EIN)|(SHAMROCK)|(Air Finland)|(OF)|(FIF)|(AIR FINLAND)|(Airfix Aviation)|(FIX)|(AIRFIX)|(Air Pacific)|(FJ)|(FJI)|(PACIFIC)|(Atlantic Airways)|(RC)|(FLI)|(FAROELINE)|(Air Florida)|(QH)|(FLZ)|(AIR FLORIDA)|(Air Iceland)|(NY)|(FXI)|(FAXI)|(Air Philippines)|(2P)|(GAP)|(ORIENT PACIFIC)|(Air Guinee Express)|(2U)|(GIP)|(FUTURE EXPRESS)|(Air Greenland)|(GL)|(GRL)|(GREENLAND)|(Atlas Air)|(5Y)|(GTI)|(GIANT)|(Air Guyane)|(GG)|(GUY)|(GREEN BIRD)|(Air Bagan)|(W9)|(JAB)|(AIR BAGAN)|(Air Canada Jazz)|(QK)|(JZA)|(JAZZ)|(Atlasjet)|(KK)|(KKK)|(ATLASJET)|(Air Astana)|(KC)|(KZR)|(ASTANALINE)|(Albanian Airlines)|(LV)|(LBC)|(ALBANIAN)|(Air Alfa)|(LFA)|(Aerolane)|(XL)|(LNE)|(LAN ECUADOR)|(Atlantis European Airways)|(TD)|(LUR)|(Air Luxor)|(LK)|(LXR)|(AIRLUXOR)|(Air Mauritius)|(MK)|(MAU)|(AIRMAURITIUS)|(Air Madagascar)|(MD)|(MDG)|(AIR MADAGASCAR)|(Air Moldova)|(9U)|(MLD)|(AIR MOLDOVA)|(Air Plus Comet)|(A7)|(MPD)|(RED COMET)|(Astair)|(8D)|(Aero Contractors)|(AJ)|(NIG)|(AEROLINE)|(Aeropelican Air Services)|(OT)|(PEL)|(PELICAN)|(Aer Arann)|(RE)|(REA)|(AER ARANN)|(Air Austral)|(UU)|(REU)|(REUNION)|(Asian Spirit)|(6K)|(RIT)|(ASIAN SPIRIT)|(Air Afrique)|(RK)|(RKA)|(AIRAFRIC)|(Airlinair)|(A5)|(RLA)|(AIRLINAIR)|(Aero Lanka)|(QL)|(RLN)|(AERO LANKA)|(Air Salone)|(RNE)|(AIR SALONE)|(Armavia)|(U8)|(RNV)|(ARMAVIA)|(AeroRep)|(P5)|(RPB)|(AEROREPUBLICA)|(Aerosur)|(5L)|(RSU)|(AEROSUR)|(Aeronorte)|(RTE)|(LUZAVIA)|(Avient Aviation)|(Z3)|(SMJ)|(AVAVIA)|(Aircompany Yakutia)|(R3)|(SYL)|(AIR YAKUTIA)|(Arkefly)|(OR)|(TFL)|(ARKEFLY)|(Airlines PNG)|(CG)|(TOK)|(BALUS)|(AirTran Airways)|(FL)|(TRS)|(CITRUS)|(Air Transat)|(TS)|(TSC)|(TRANSAT)|(Avialeasing Aviation Company)|(EC)|(TWN)|(TWINARROW)|(Austrian Arrows)|(VO)|(TYR)|(TYROLEAN)|(Alrosa Mirny Air Enterprise)|(6R)|(DRU)|(MIRNY)|(British Airways)|(BA)|(BAW)|(SPEEDBIRD)|(Biman Bangladesh Airlines)|(BG)|(BBC)|(BANGLADESH)|(Belair Airlines)|(4T)|(BHP)|(BELAIR)|(Bahamasair)|(UP)|(BHS)|(BAHAMAS)|(Balkan Bulgarian Airlines)|(LZ)|(British International Helicopters)|(BS)|(BIH)|(BRINTEL)|(Bangkok Airways)|(PG)|(BKP)|(BANGKOK AIR)|(Blue1)|(KF)|(BLF)|(BLUEFIN)|(Baltic Airlines)|(BLL)|(BALTIC AIRLINES)|(Bearskin Lake Air Service)|(JV)|(BLS)|(BEARSKIN)|(Bellview Airlines)|(B3)|(BLV)|(BELLVIEW AIRLINES)|(bmi)|(bmi British Midland)|(BD)|(BMA)|(MIDLAND)|(bmibaby)|(WW)|(BMI)|(BABY)|(Bemidji Airlines)|(CH)|(BMJ)|(BEMIDJI)|(British Midland Regional)|(BMR)|(Blue Panorama Airlines)|(BV)|(BPA)|(BLUE PANOROMA)|(Bering Air)|(8E)|(BRG)|(BERING AIR)|(Brazilian Air Force)|(BRS)|(BRAZILIAN AIR FORCE)|(Belavia Belarusian Airlines)|(B2)|(BRU)|(BELARUS AVIA)|(Metro Batavia)|(7P)|(BTV)|(BATAVIA)|(Berjaya Air)|(J8)|(BVT)|(BERJAYA)|(Blue Wings)|(QW)|(BWG)|(BLUE WINGS)|(Brit Air)|(DB)|(BZH)|(BRITAIR)|(Binter Canarias)|(NT)|(IBB)|(Blue Air)|(0B)|(JOR)|(BLUE TRANSPORT)|(British Mediterranean Airways)|(KJ)|(LAJ)|(BEE MED)|(Bulgaria Air)|(FB)|(LZB)|(FLYING BULGARIA)|(Barents AirLink)|(8N)|(NKF)|(NORDFLIGHT)|(CAL Cargo Air Lines)|(5C)|(ICL)|(CAL)|(Calima Aviacion)|(XG)|(CLI)|(CALIMA)|(Canadian Airlines)|(CP)|(CDN)|(CANADIAN)|(Canadian North)|(5T)|(MPE)|(EMPRESS)|(Cape Air)|(9K)|(KAP)|(CAIR)|(Caribbean Airlines)|(BW)|(BWA)|(CARIBBEAN AIRLINES)|(Carpatair)|(V3)|(KRP)|(CARPATAIR)|(Caspian Airlines)|(RV)|(CPN)|(CASPIAN)|(Cathay Pacific)|(CX)|(CPA)|(CATHAY)|(Cayman Airways)|(KX)|(CAY)|(CAYMAN)|(Cebu Pacific)|(5J)|(CEB)|(CEBU AIR)|(Central Connect Airlines)|(CCG)|(Centralwings)|(C0)|(CLW)|(CENTRALWINGS)|(Charter Air)|(CHW)|(CHARTER WIEN)|(Chautauqua Airlines)|(RP)|(CHQ)|(CHAUTAUQUA)|(China Airlines)|(CI)|(CAL)|(DYNASTY)|(China Eastern Airlines)|(MU)|(CES)|(CHINA EASTERN)|(China Southern Airlines)|(CZ)|(CSN)|(CHINA SOUTHERN)|(China United Airlines)|(HR)|(CUA)|(LIANHANG)|(Yunnan Airlines)|(3Q)|(CYH)|(YUNNAN)|(Cimber Air)|(QI)|(CIM)|(CIMBER)|(Cirrus Airlines)|(C9)|(RUS)|(CIRRUS AIR)|(City Airline)|(CF)|(SDR)|(SWEDESTAR)|(City Connexion Airlines)|(G3)|(CIX)|(CONNEXION)|(BA CityFlyer)|(CJ)|(CFE)|(FLYER)|(Click Airways)|(CGK)|(CLICK AIR)|(Colgan Air)|(9L)|(CJC)|(COLGAN)|(Comair)|(OH)|(COM)|(COMAIR)|(Comair)|(MN)|(CAW)|(COMMERCIAL)|(CommutAir)|(C5)|(UCA)|(COMMUTAIR)|(Comores Airlines)|(KR)|(CWK)|(CONTICOM)|(Compass Airlines)|(CP)|(CPZ)|(Compass Rose)|(Condor Flugdienst)|(DE)|(CFG)|(CONDOR)|(Consorcio Aviaxsa)|(6A)|(CHP)|(AVIACSA)|(Contact Air)|(Contactair)|(C3)|(KIS)|(CONTACTAIR)|(Continental Airlines)|(CO)|(COA)|(CONTINENTAL)|(Continental Express)|(CO)|(JETLINK)|(Continental Micronesia)|(CS)|(CMI)|(AIR MIKE)|(Conviasa)|(V0)|(VCV)|(CONVIASA)|(Copa Airlines)|(CM)|(CMP)|(COPA)|(Copterline)|(AAQ)|(COPTERLINE)|(Corendon Airlines)|(CAI)|(CORENDON)|(Corsairfly)|(SS)|(CRL)|(CORSAIR)|(Crest Aviation)|(CAN)|(CREST)|(Croatia Airlines)|(OU)|(CTN)|(CROATIA)|(Crown Airways)|(CRO)|(CROWN AIRWAYS)|(Cyprus Airways)|(CY)|(CYP)|(CYPRUS)|(Cyprus Turkish Airlines)|(YK)|(DAT Danish Air Transport)|(DX)|(DTR)|(DANISH)|(Daallo Airlines)|(D3)|(DAO)|(DALO AIRLINES)|(Dalavia)|(H8)|(KHB)|(DALAVIA)|(Darwin Airline)|(0D)|(DWT)|(DARWIN)|(Delta Aerotaxi)|(DEA)|(JET SERVICE)|(Delta Air Lines)|(DL)|(DAL)|(DELTA)|(Denim Air)|(DNM)|(DENIM)|(Deutsche Bahn)|(2A)|(Djibouti Airlines)|(D8)|(DJB)|(DJIBOUTI AIR)|(Dniproavia)|(UDN)|(DNIEPRO)|(Dominicana de Aviaci)|(DO)|(DOA)|(DOMINICANA)|(Domodedovo Airlines)|(E3)|(DMO)|(DOMODEDOVO)|(DonbassAero)|(5D)|(UDC)|(DONBASS AERO)|(Dragonair)|(KA)|(HDA)|( Hong Kong Dragon Airlines)|(Druk Air)|(KB)|(DRK)|(ROYAL BHUTAN)|(Dubrovnik Air)|(DBK)|(SEAGULL)|(Dutch Antilles Express)|(DNL)|(DUTCH ANTILLES)|(dba)|(DI)|(BAG)|(SPEEDWAY)|(EVA Air)|(BR)|(EVA)|(EVA)|(Eagle Air)|(H7)|(East African)|(QU)|(UGX)|(CRANE)|(Eastern Airways)|(T3)|(EZE)|(EASTFLIGHT)|(Eastland Air)|(DK)|(ELA)|(Ecuavia)|(ECU)|(ECUAVIA)|(Edelweiss Air)|(WK)|(EDW)|(EDELWEISS)|(Egyptair)|(MS)|(MSR)|(EGYPTAIR)|(El Al Israel Airlines)|(LY)|(ELY)|(ELAL)|(Emirates)|(Emirates Airlines)|(EK)|(UAE)|(EMIRATES)|(Empresa Ecuatoriana De Aviacion)|(EU)|(EEA)|(ECUATORIANA)|(Eritrean Airlines)|(B8)|(ERT)|(ERITREAN)|(Estonian Air)|(OV)|(ELL)|(ESTONIAN)|(Ethiopian Airlines)|(ET)|(ETH)|(ETHIOPIAN)|(Etihad Airways)|(EY)|(ETD)|(ETIHAD)|(Euro Exec Express)|(RZ)|(Eurocypria Airlines)|(UI)|(ECA)|(EUROCYPRIA)|(Eurofly Service)|(GJ)|(EEU)|(EUROFLY)|(Eurolot)|(K2)|(ELO)|(EUROLOT)|(European Air Express)|(EA)|(EAL)|(STAR WING)|(Eurowings)|(EW)|(EWG)|(EUROWINGS)|(Evergreen International Airlines)|(EZ)|(EIA)|(EVERGREEN)|(Excel Airways)|(JN)|(XLA)|(EXPO)|(Excel Charter)|(XEL)|(HELI EXCEL)|(Express One International)|(EO)|(LHN)|(LONGHORN)|(ExpressJet)|(XE)|(BTA)|(JET LINK)|(easyJet)|(EasyJet Airline)|(U2)|(EZY)|(EASY)|(Far Eastern Air Transport)|(EF)|(EFA)|(Far Eastern)|(Finnair)|(AY)|(FIN)|(FINNAIR)|(Finncomm Airlines)|(FC)|(WBA)|(WESTBIRD)|(Firefly)|(FY)|(FFM)|(FIREFLY)|(First Air)|(7F)|(FAB)|(First Choice Airways)|(DP)|(FCA)|(JETSET)|(Flightline)|(B5)|(FLT)|(FLIGHTLINE)|(Florida West International Airways)|(RF)|(FWL)|(FLO WEST)|(AirAsia X)|(FlyAsianXpress)|(D7)|(XAX)|(XANADU)|(FlyLal)|(TE)|(LIL)|(LITHUANIA AIR)|(FlyNordic)|(LF)|(NDC)|(NORDIC)|(Flybaboo)|(F7)|(BBO)|(BABOO)|(Flybe)|(BE)|(BEE)|(JERSEY)|(Flyglobespan)|(B4)|(GSM)|(GLOBESPAN)|(Flyhy Cargo Airlines)|(FYH)|(FLY HIGH)|(Formosa Airlines)|(VY)|(FOS)|(Freedom Air)|(FP)|(FRE)|(FREEDOM)|(Freedom Airlines)|(FRL)|(FREEDOM AIR)|(Frontier Airlines)|(F9)|(FFT)|(FRONTIER FLIGHT)|(GB Airways)|(GT)|(GBL)|(GEEBEE AIRWAYS)|(Garuda Indonesia)|(GA)|(GIA)|(INDONESIA)|(Gazpromavia)|(4G)|(GZP)|(GAZPROMAVIA)|(Georgian Airways)|(A9)|(TGZ)|(TAMAZI)|(Georgian National Airlines)|(QB)|(GFG)|(NATIONAL)|(Germania)|(ST)|(GMI)|(GERMANIA)|(Germanwings)|(4U)|(GWI)|(GERMAN WINGS)|(Ghana International Airlines)|(G0)|(GHB)|(GHANA AIRLINES)|(Go Air)|(G8)|(GOW)|(GOAIR)|(GoJet Airlines)|(G7)|(GJS)|(GATEWAY)|(Golden Air)|(DC)|(GAO)|(GOLDEN)|(Great Lakes Airlines)|(ZK)|(GLA)|(LAKES AIR)|(Gulf Air)|(GFA)|(GULF AIR)|(Gulf Air Bahrain)|(GF)|(GBA)|(GULF BAHRAIN)|(Gulfstream International Airlines)|(GFT)|(GULF FLIGHT)|(Hageland Aviation Services)|(H6)|(HAG)|(HAGELAND)|(Hainan Airlines)|(HU)|(CHH)|(HAINAN)|(Haiti Ambassador Airlines)|(2T)|(HAM)|(Hamburg International)|(4R)|(HHI)|(HAMBURG JET)|(TUIfly)|(X3)|(HLX)|(YELLOW CAB)|(Hapagfly)|(HF)|(HLF)|(HAPAG LLOYD)|(Hawaiian Airlines)|(HA)|(HAL)|(HAWAIIAN)|(Hawkair)|(BH)|(Heli France)|(8H)|(HFR)|(HELIFRANCE)|(Helijet)|(JB)|(JBA)|(HELIJET)|(Hellas Jet)|(T4)|(HEJ)|(HELLAS JET)|(Hello)|(HW)|(FHE)|(FLYHELLO)|(Helvetic Airways)|(2L)|(OAW)|(HELVETIC)|(Highland Airways)|(HWY)|(HIWAY)|(Hokkaido International Airlines)|(HD)|(ADO)|(AIR DO)|(Hong Kong Airlines)|(HX)|(CRK)|(BAUHINIA)|(Hong Kong Express Airways)|(UO)|(HKE)|(HONGKONG SHUTTLE)|(Horizon Air)|(Horizon Airlines)|(QX)|(QXE)|(HORIZON AIR)|(Horizon Airlines)|(BN)|(HZA)|(Iberia Airlines)|(IB)|(IBE)|(IBERIA)|(Iberworld)|(TY)|(IWD)|(Ibex Airlines)|(FW)|(IBX)|(IBEX)|(Icar Air)|(RAC)|(TUZLA AIR)|(Icelandair)|(FI)|(ICE)|(ICEAIR)|(Imair Airlines)|(IK)|(ITX)|(IMPROTEX)|(IndiGo Airlines)|(6E)|(IGO)|(IFLY)|(Indian Airlines)|(IC)|(IAC)|(INDAIR)|(Indigo)|(I9)|(IBU)|(INDIGO BLUE)|(Indonesia AirAsia)|(QZ)|(AWQ)|(WAGON AIR)|(Indonesian Airlines)|(IO)|(IAA)|(INDO LINES)|(Interair South Africa)|(D6)|(ILN)|(INLINE)|(Interavia Airlines)|(ZA)|(SUW)|(ASTAIR)|(Interlink Airlines)|(ID)|(ITK)|(INTERLINK)|(Intersky)|(3L)|(ISK)|(INTERSKY)|(Iran Air)|(IR)|(IRA)|(IRANAIR)|(Iran Aseman Airlines)|(EP)|(IRC)|(Iraqi Airways)|(IA)|(IAW)|(IRAQI)|(Island Airlines)|(IS)|(Cargo Plus Aviation)|(8L)|(CGP)|(Islas Airways)|(IF)|(ISW)|(PINTADERA)|(Islena De Inversiones)|(WC)|(ISV)|(Israir)|(6H)|(ISR)|(ISRAIR)|(JAL Express)|(JC)|(JEX)|(JANEX)|(JALways)|(JO)|(JAZ)|(JALWAYS)|(Japan Airlines)|(JAL Japan Airlines)|(JL)|(JAL)|(JAPANAIR)|(Japan Asia Airways)|(EG)|(JAA)|(ASIA)|(Japan Transocean Air)|(NU)|(JTA)|(JAI OCEAN)|(Jat Airways)|(JU)|(JAT)|(JAT)|(Jazeera Airways)|(J9)|(JZR)|(JAZEERA)|(Jeju Air)|(7C)|(JJA)|(JEJU AIR)|(Jet Airways)|(9W)|(JAI)|(JET AIRWAYS)|(Jet Airways)|(QJ)|(Jetstar Asia Airways)|(3K)|(JSA)|(JETSTAR ASIA)|(Jet4You)|(8J)|(JFU)|(ARGAN)|(JetBlue Airways)|(B6)|(JBU)|(JETBLUE)|(Jetairfly)|(JF)|(JAF)|(BEAUTY)|(Jetflite)|(JEF)|(JETFLITE)|(Jetstar Airways)|(JQ)|(JST)|(JETSTAR)|(Juneyao Airlines)|(HO)|(DKH)|(JUNEYAO AIRLINES)|(KD Avia)|(KD)|(KNI)|(KALININGRAD AIR)|(KLM Cityhopper)|(WA)|(KLC)|(CITY)|(KLM Royal Dutch Airlines)|(KL)|(KLM)|(KLM)|(Kam Air)|(RQ)|(KMF)|(KAMGAR)|(Kavminvodyavia)|(KV)|(MVD)|(AIR MINVODY)|(Kendell Airlines)|(KDA)|(KENDELL)|(Kenmore Air)|(M5)|(KEN)|(KENMORE)|(Kenya Airways)|(KQ)|(KQA)|(KENYA)|(Kingfisher Airlines)|(IT)|(KFR)|(KINGFISHER)|(Kish Air)|(Y9)|(IRK)|(KISHAIR)|(Kogalymavia Air Company)|(7K)|(KGL)|(KOGALYM)|(Korean Air)|(KE)|(KAL)|(KOREANAIR)|(Kosmos)|(KSM)|(KOSMOS)|(Krasnojarsky Airlines)|(7B)|(KJC)|(KRASNOJARSKY AIR)|(Kuban Airlines)|(GW)|(KIL)|(AIR KUBAN)|(Kuwait Airways)|(KU)|(KAC)|(KUWAITI)|(Kuzu Airlines Cargo)|(GO)|(KZU)|(KUZU CARGO)|(LACSA)|(LR)|(LRC)|(LACSA)|(LAN Airlines)|(LA)|(LAN)|(LAN)|(LAN Argentina)|(4M)|(DSM)|(LAN AR)|(LAN Express)|(LU)|(LXP)|(LANEX)|(LAN Peru)|(LP)|(LPE)|(LANPERU)|(LOT Polish Airlines)|(LO)|(LOT)|(POLLOT)|(LTE International Airways)|(XO)|(LTE)|(FUN JET)|(LTU Austria)|(L3)|(LTO)|(BILLA TRANSPORT)|(LTU International)|(LT)|(LTU)|(LTU)|(Lao Airlines)|(QV)|(LAO)|(LAO)|(LatCharter)|(LTC)|(LATCHARTER)|(Lauda Air)|(NG)|(LDA)|(LAUDA AIR)|(Leeward Islands Air Transport)|(LI)|(LIA)|(LIAT)|(Libyan Arab Airlines)|(LN)|(LAA)|(LIBAIR)|(Linhas A)|(LM)|(LAM)|(MOZAMBIQUE)|(Lion Mentari Airlines)|(JT)|(LNI)|(LION INTER)|(Luftfahrtgesellschaft Walter)|(HE)|(LGW)|(WALTER)|(Lufthansa)|(LH)|(DLH)|(LUFTHANSA)|(Lufthansa Cargo)|(LH)|(GEC)|(LUFTHANSA CARGO)|(Lufthansa CityLine)|(CL)|(CLH)|(HANSALINE)|(Lufttransport)|(L5)|(LTR)|(LUFT TRANSPORT)|(Luxair)|(LG)|(LGL)|(LUXAIR)|(L)|(MJ)|(LPR)|(LAPA)|(MasAir)|(M7)|(MAA)|(MAS CARGA)|(MAT Macedonian Airlines)|(IN)|(MAK)|(MAKAVIO)|(MIAT Mongolian Airlines)|(OM)|(MGL)|(MONGOL AIR)|(MNG Airlines)|(MB)|(MNB)|(BLACK SEA)|(Macair Airlines)|(CC)|(MCK)|(Maersk)|(DM)|(Mahan Air)|(W5)|(IRM)|(MAHAN AIR)|(Malaysia Airlines)|(MH)|(MAS)|(MALAYSIAN)|(Malmo Aviation)|(SCW)|(SCANWING)|(Malta Air Charter)|(R5)|(MAC)|(MALTA CHARTER)|(Mandala Airlines)|(RI)|(MDL)|(MANDALA)|(Mandarin Airlines)|(AE)|(MDA)|(Mandarin)|(Mango)|(JE)|(MNO)|(TULCA)|(Martinair)|(MP)|(MPH)|(MARTINAIR)|(Maxair)|(8M)|(MXL)|(MAXAIR)|(Maya Island Air)|(MW)|(MYD)|(MYLAND)|(Meridiana)|(IG)|(ISS)|(MERAIR)|(Merpati Nusantara Airlines)|(MZ)|(MNA)|(MERPATI)|(Mesa Airlines)|(YV)|(ASH)|(AIR SHUTTLE)|(Mesaba Airlines)|(XJ)|(MES)|(MESABA)|(Mexicana de Aviaci)|(MX)|(MXA)|(MEXICANA)|(Middle East Airlines)|(ME)|(MEA)|(CEDAR JET)|(Midway Airlines)|(JI)|(MDW)|(MIDWAY)|(Midwest Airlines)|(YX)|(MEP)|(Moldavian Airlines)|(2M)|(MDV)|(MOLDAVIAN)|(Monarch Airlines)|(ZB)|(MON)|(MONARCH)|(Myway Airlines)|(8I)|(Montenegro Airlines)|(YM)|(MGX)|(MONTAIR)|(Morningstar Air Express)|(MAL)|(MORNINGSTAR)|(Moskovia Airlines)|(3R)|(GAI)|(GROMOV AIRLINE)|(Motor Sich)|(M9)|(MSI)|(MOTOR SICH)|(MyTravel Airways)|(VZ)|(MYT)|(KESTREL)|(Myanma Airways)|(UB)|(UBA)|(UNIONAIR)|(Myanmar Airways International)|(8M)|(MMM)|(assignment postponed)|(Myflug)|(MYA)|(MYFLUG)|(Nasair)|(UE)|(NAS)|(NASAIRWAYS)|(National Jet Systems)|(NC)|(NJS)|(NATIONAL JET)|(Nationwide Airlines)|(CE)|(NTW)|(NATIONWIDE)|(Nauru Air Corporation)|(ON)|(RON)|(AIR NAURU)|(Nepal Airlines)|(RA)|(RNA)|(ROYAL NEPAL)|(NetJets)|(1I)|(EJA)|(EXECJET)|(New England Airlines)|(EJ)|(NEA)|(NEW ENGLAND)|(NextJet)|(2N)|(NTJ)|(NEXTJET)|(Niki)|(HG)|(NLY)|(FLYNIKI)|(Nok Air)|(DD)|(NOK)|(NOK AIR)|(Norfolk County Flight College)|(NCF)|(COUNTY)|(North American Airlines)|(NTM)|(NORTHAM)|(North American Charters)|(HMR)|(HAMMER)|(Northern Dene Airways)|(U7)|(Northwest Airlines)|(NW)|(NWA)|(NORTHWEST)|(Northwestern Air)|(J3)|(PLR)|(POLARIS)|(Norwegian Air Shuttle)|(DY)|(NAX)|(NOR SHUTTLE)|(Norwegian Aviation College)|(TFN)|(SPRIT)|(Nouvel Air Tunisie)|(BJ)|(LBT)|(NOUVELAIR)|(Novair)|(1I)|(NVR)|(NAVIGATOR)|(Nas Air)|(XY)|(KNE)|(NAS EXPRESS)|(Oasis Hong Kong Airlines)|(O8)|(OHK)|(OASIS)|(Ocean Air)|(BCN)|(BLUE OCEAN)|(Oceanair)|(O6)|(ONE)|(OCEANAIR)|(Oceanic Airlines)|(O2)|(Olympic Airlines)|(OA)|(OAL)|(OLYMPIC)|(Oman Air)|(WY)|(OMA)|(OMAN AIR)|(One Two Go Airlines)|(OTG)|(THAI EXPRESS)|(Onur Air)|(8Q)|(OHY)|(ONUR AIR)|(Orenburg Airlines)|(R2)|(ORB)|(ORENBURG)|(Orient Thai Airlines)|(OX)|(OEA)|(ORIENT THAI)|(Origin Pacific Airways)|(QO)|(OGN)|(ORIGIN)|(Ostfriesische Lufttransport)|(OL)|(OLT)|(OLTRA)|(Overland Airways)|(OJ)|(OLA)|(OVERLAND)|(Ozjet Airlines)|(O7)|(OZJ)|(AUSJET)|(PAN Air)|(PV)|(PNR)|(SKYJET)|(PB Air)|(9Q)|(PBA)|(PEEBEE AIR)|(PLUNA)|(PU)|(PUA)|(PLUNA)|(PMTair)|(U4)|(PMT)|(MULTITRADE)|(Jetstar Pacific)|(Pacific Airlines)|(BL)|(PIC)|(PACIFIC AIRLINES)|(Pacific Coastal Airline)|(8P)|(PCO)|(PASCO)|(Pacific Island Aviation)|(PSA)|(PACIFIC ISLE)|(Pacific Wings)|(LW)|(NMI)|(TSUNAMI)|(Pakistan International Airlines)|(PIA Pakistan International)|(PK)|(PIA)|(PAKISTAN)|(Paramount Airways)|(I7)|(PMW)|(PARAWAY)|(Passaredo Transportes Aereos)|(PTB)|(PASSAREDO)|(Pegasus Airlines)|(PC)|(PGT)|(SUNTURK)|(Peninsula Airways)|(KS)|(PEN)|(PENINSULA)|(Philippine Airlines)|(PR)|(PAL)|(PHILIPPINE)|(Pinnacle Airlines)|(9E)|(FLG)|(FLAGSHIP)|(Polet)|(POT)|(POLET)|(Polynesian Airlines)|(PH)|(PAO)|(POLYNESIAN)|(Porter Airlines)|(PD)|(POE)|(PORTER AIR)|(Portugalia)|(NI)|(PGA)|(PORTUGALIA)|(Potomac Air)|(BK)|(PDC)|(DISTRICT)|(Precision Air)|(PW)|(PRF)|(PRECISION AIR)|(Privatair)|(PTI)|(PRIVATAIR)|(Proflight Commuter Services)|(P0)|(Qantas)|(Qantas Airways)|(QF)|(QFA)|(QANTAS)|(Qatar Airways)|(QR)|(QTR)|(QATARI)|(RACSA)|(R6)|(Kinloss Flying Training Unit)|(KIN)|(KINLOSS)|(Regional Airlines)|(FN)|(Regional Express)|(ZL)|(RXA)|(REX)|(Republic Airlines)|(RW)|(RPA)|(BRICKYARD)|(Republic Express Airlines)|(RH)|(RPH)|(PUBLIC EXPRESS)|(Rossiya)|(R4)|(Air Rarotonga)|(GZ)|(RAR)|(Royal Air Maroc)|(AT)|(RAM)|(ROYALAIR MAROC)|(Royal Brunei Airlines)|(BI)|(RBA)|(BRUNEI)|(Royal Jordanian)|(RJ)|(RJA)|(JORDANIAN)|(Royal Nepal Airlines)|(RA)|(RNA)|(ROYAL NEPAL)|(Rusline)|(RLU)|(RUSLINE AIR)|(Rwandair Express)|(WB)|(RWD)|(RWANDAIR)|(Ryan Air Services)|(RYA)|(RYAN AIR)|(Ryan International Airlines)|(RD)|(RYN)|(RYAN INTERNATIONAL)|(Ryanair)|(FR)|(RYR)|(RYANAIR)|(SATA International)|(S4)|(RZO)|(AIR AZORES)|(South African Airways)|(SAA South African Airways)|(SA)|(SAA)|(SPRINGBOK)|(Shaheen Air International)|(NL)|(SAI)|(SHAHEEN AIR)|(Scandinavian Airlines System)|(SAS Scandinavian Airlines)|(SK)|(SAS)|(SCANDINAVIAN)|(ScotAirways)|(SAY)|(SUCKLING)|(S7 Airlines)|(Sibir Airlines)|(S7)|(SBI)|(SIBERIAN AIRLINES)|(Seaborne Airlines)|(BB)|(SBS)|(SEABORNE)|(Scenic Airlines)|(SCE)|(SCENIC)|(SriLankan Airlines)|(UL)|(ALK)|(SRILANKAN)|(Sun Country Airlines)|(SY)|(SCX)|(SUN COUNTRY)|(Southeast Air)|(SEA)|(SOUTHEAST AIR)|(Sky Express)|(G3)|(SEH)|(AIR CRETE)|(Spicejet)|(SG)|(SEJ)|(SPICEJET)|(Star Flyer)|(7G)|(SFJ)|(STARFLYER)|(Skagway Air Service)|(N5)|(SGY)|(SKAGWAY AIR)|(Sahara Airlines)|(SHD)|(SATA Air Acores)|(SP)|(SAT)|(SATA)|(Singapore Airlines)|(SQ)|(SIA)|(SINGAPORE)|(Sibaviatrans)|(5M)|(SIB)|(SIBAVIA)|(Skynet Airlines)|(SI)|(SIH)|(BLUEJET)|(Sriwijaya Air)|(SJ)|(SJY)|(SRIWIJAYA)|(Sama Airlines)|(ZS)|(SMY)|(NAJIM)|(Singapore Airlines Cargo)|(SQ)|(SQC)|(SINGCARGO)|(Siem Reap Airways)|(FT)|(SRH)|(SIEMREAP AIR)|(South East Asian Airlines)|(DG)|(SRQ)|(SEAIR)|(Skyservice Airlines)|(5G)|(SSV)|(SKYTOUR)|(Servicios de Transportes A)|(FS)|(STU)|(FUEGUINO)|(Sudan Airways)|(SD)|(SUD)|(SUDANAIR)|(Saudi Arabian Airlines)|(SV)|(SVA)|(SAUDIA)|(Southwest Airlines)|(WN)|(SWA)|(SOUTHWEST)|(Southern Winds Airlines)|(A4)|(SWD)|(SOUTHERN WINDS)|(Swiss International Air Lines)|(Swiss Airlines)|(LX)|(SWR)|(SWISS)|(Swissair)|(SR)|(SWR)|(Swissair)|(Swiss European Air Lines)|(Swiss European)|(SWU)|(EUROSWISS)|(Swe Fly)|(WV)|(SWV)|(FLYING SWEDE)|(SunExpress)|(XQ)|(SXS)|(SUNEXPRESS)|(Syrian Arab Airlines)|(RB)|(SYR)|(SYRIANAIR)|(Shandong Airlines)|(SC)|(CDG)|(SHANDONG)|(SAS Braathens)|(CNO)|(SCANOR)|(Spring Airlines)|(9S)|(CQH)|(AIR SPRING)|(Sichuan Airlines)|(3U)|(CSC)|(SI CHUAN)|(Shanghai Airlines)|(FM)|(CSH)|(SHANGHAI AIR)|(Shenzhen Airlines)|(ZH)|(CSZ)|(SHENZHEN AIR)|(SkyEurope)|(NE)|(ESK)|(RELAX)|(Sky Europe Airlines)|(HSK)|(MATRA)|(Spanair)|(JK)|(JKK)|(SPANAIR)|(Spirit Airlines)|(NK)|(NKS)|(SPIRIT WINGS)|(SATENA)|(9R)|(NSE)|(SATENA)|(Skywest Airlines)|(OZW)|(OZWEST)|(Santa Barbara Airlines)|(S3)|(BBR)|(SANTA BARBARA)|(Sky Airline)|(H2)|(SKU)|(AEROSKY)|(SkyWest)|(OO)|(SKW)|(SKYWEST)|(Skyways Express)|(JZ)|(SKX)|(SKY EXPRESS)|(Skymark Airlines)|(BC)|(SKY)|(SKYMARK)|(SilkAir)|(MI)|(SLK)|(SILKAIR)|(Surinam Airways)|(PY)|(SLM)|(SURINAM)|(Sterling Airlines)|(NB)|(SNB)|(STERLING)|(Skynet Asia Airways)|(6J)|(SNJ)|(NEWSKY)|(Solomon Airlines)|(IE)|(SOL)|(SOLOMON)|(Southern Airways)|(SOU)|(SOUTHERN EXPRESS)|(Saratov Aviation Division)|(6W)|(SOV)|(SARATOV AIR)|(Sat Airlines)|(HZ)|(SOZ)|(SATCO)|(South Pacific Island Airways)|(SPI)|(SOUTH PACIFIC)|(Shuttle America)|(S5)|(TCF)|(MERCURY)|(Scat Air)|(DV)|(VSV)|(VLASTA)|(TAME)|(EQ)|(TAE)|(TAME)|(TAM Brazilian Airlines)|(JJ)|(TAM)|(TAM)|(TAP Portugal)|(TAP Air Portugal)|(TP)|(TAP)|(AIR PORTUGAL)|(Tunisair)|(TU)|(TAR)|(TUNAIR)|(Thai Air Cargo)|(T2)|(TCG)|(THAI CARGO)|(Thomas Cook Airlines)|(FQ)|(TCW)|(THOMAS COOK)|(Thomas Cook Airlines)|(MT)|(TCX)|(KESTREL)|(Trigana Air Service)|(TGN)|(TRIGANA)|(Tiger Airways)|(TR)|(TGW)|(GO CAT)|(Tiger Airways Australia)|(TT)|(TGW)|(GO CAT)|(Thai Airways International)|(TG)|(THA)|(THAI)|(Turk Hava Kurumu Hava Taksi Isletmesi)|(THK)|(HUR KUS)|(Thai AirAsia)|(Thai Air Asia)|(FD)|(AIQ)|(THAI ASIA)|(Turkish Airlines)|(TK)|(THY)|(TURKAIR)|(Tajikistan International Airlines)|(TIL)|(TIL)|(Twin Jet)|(T7)|(TJT)|(TWINJET)|(Translift Airways)|(TLA)|(TRANSLIFT)|(Trans Mediterranean Airlines)|(TL)|(TMA)|(TANGO LIMA)|(Tiara Air)|(3P)|(TNM)|(TIARA)|(Thomsonfly)|(BY)|(TOM)|(TOMSON)|(Tropic Air)|(PM)|(TOS)|(TROPISER)|(TAMPA)|(QT)|(TPA)|(TAMPA)|(TransAsia Airways)|(GE)|(TNA)|(TransAsia)|(Transavia Holland)|(HV)|(TRA)|(TRANSAVIA)|(TACV)|(VR)|(TCV)|(CABOVERDE)|(Transwest Air)|(9T)|(ABS)|(ATHABASKA)|(Transaero Airlines)|(UN)|(TSO)|(TRANSOVIET)|(Turkmenistan Airlines)|(Turkmenhovayollary)|(T5)|(TUA)|(TURKMENISTAN)|(Tuninter)|(UG)|(TUI)|(Travel Service)|(QS)|(TVS)|(SKYTRAVEL)|(TUIfly Nordic)|(6B)|(BLX)|(BLUESCAN)|(TAAG Angola Airlines)|(DT)|(DTA)|(DTA)|(Turkish Air Force)|(HVK)|(TURKISH AIRFORCE)|(TAM Mercosur)|(PZ)|(LAP)|(PARAGUAYA)|(Trans States Airlines)|(AX)|(LOF)|(WATERSKI)|(Tarom)|(RO)|(ROT)|(TAROM)|(Turan Air)|(3T)|(URN)|(TURAN)|(TRIP Linhas A)|(8R)|(TIB)|(TRIP)|(USA3000 Airlines)|(U5)|(GWY)|(GETAWAY)|(United Airlines)|(UA)|(UAL)|(UNITED)|(United Air Charters)|(UAC)|(UNITAIR)|(Ural Airlines)|(U6)|(SVR)|(SVERDLOVSK AIR)|(UM Airlines)|(UF)|(UKM)|(UKRAINE MEDITERRANEE)|(US Airways)|(US)|(USA)|(U S AIR)|(UTair Aviation)|(UT)|(UTA)|(UTAIR)|(United States Air Force)|(AIO)|(AIR CHIEF)|(Uzbekistan Airways)|(HY)|(UZB)|(UZBEK)|(Ukraine International Airlines)|(PS)|(AUI)|(UKRAINE INTERNATIONAL)|(US Helicopter Corporation)|(UH)|(Valuair)|(VF)|(VLU)|(VALUAIR)|(Vasco Air)|(VFC)|(VASCO AIR)|(Vietnam Airlines)|(VN)|(HVN)|(VIET NAM AIRLINES)|(VIM Airlines)|(NN)|(MOV)|(MOV AIR)|(Volaris)|(Y4)|(VOI)|(VOLARIS)|(Virgin America)|(VX)|(VRD)|(REDWOOD)|(Virgin Express)|(TV)|(VEX)|(VIRGIN EXPRESS)|(Virgin Nigeria Airways)|(VK)|(VGN)|(VIRGIN NIGERIA)|(Virgin Atlantic Airways)|(VS)|(VIR)|(VIRGIN)|(Viva Macau)|(ZG)|(VVM)|(JACKPOT)|(Volare Airlines)|(VE)|(VLE)|(VOLA)|(Vueling Airlines)|(VY)|(VLG)|(VUELING)|(Vladivostok Air)|(XF)|(VLK)|(VLADAIR)|(Varig Log)|(LC)|(VLO)|(VELOG)|(Virgin Australia)|(VA)|(VOZ)|(VIRGIN)|(VRG Linhas Aereas)|(Varig)|(RG)|(VRN)|(VARIG)|(VASP)|(VP)|(VSP)|(VASP)|(VLM Airlines)|(VG)|(VLM)|(RUBENS)|(Wayraper)|(7W)|(WAYRAPER)|(Welcome Air)|(2W)|(WLC)|(WELCOMEAIR)|(West Coast Air)|(8O)|(WestJet)|(WS)|(WJA)|(WESTJET)|(Western Airlines)|(WA)|(WAL)|(WESTERN)|(Wind Jet)|(IV)|(JET)|(GHIBLI)|(Wings Air)|(IW)|(WON)|(WINGS ABADI)|(Wizz Air)|(W6)|(WZZ)|(WIZZ AIR)|(Wizz Air Hungary)|(8Z)|(WVL)|(WIZZBUL)|(World Airways)|(WO)|(WOA)|(WORLD)|(XL Airways France)|(SE)|(SEU)|(STARWAY)|(Xiamen Airlines)|(MF)|(CXA)|(XIAMEN AIR)|(Yamal Airlines)|(YL)|(LLM)|(YAMAL)|(Yemenia)|(IY)|(IYE)|(YEMENI)|(Yuzhmashavia)|(UMK)|(YUZMASH)|(Zanair)|(TAN)|(ZANAIR)|(Zoom Airlines)|(Z4)|(OOM)|(ZOOM)|(Tyrolean Airways)|(N)|(TYR)|(TYROLEAN)|(Maldivian Air Taxi)|(8Q)|(N)|(N)|(Royal Air Cambodge)|(VJ)|(RAC)|(Air Mandalay)|(6T)|(N)|(Six Tango)|(Air Busan)|(BX)|(ABL)|(Air Busan)|(Sky Express)|(SkyExpress)|(XW)|(SXR)|(SKYSTORM)|(Globus)|(GH)|(GLP)|(Air Kazakhstan)|(9Y)|(KZK)|(Kazakh)|(Japan Air System)|(JD)|(JAS)|(Air System)|(Carnival Air Lines)|(KW)|(N)|(Carnival Air)|(United Airways)|(4H)|(UBD)|(UNITED BANGLADESH)|(Fly540)|(5H)|(FFV)|(SWIFT TANGO)|(Transavia France)|(TO)|(TVF)|(FRENCH SUN)|(Uni Air)|(B7)|(UIA)|(Glory)|(Gomelavia)|(YD)|(N)|(Red Wings)|(Avialinii 400)|(WZ)|(RWZ)|(AIR RED)|(Felix Airways)|(FU)|(FXX)|(Kostromskie avialinii)|(K1)|(KOQ)|(Greenfly)|(XX)|(GFY)|(Tajik Air)|(7J)|(N)|(Air Mozambique)|(TM)|(N)|(Gabon Airlines)|(GY)|(GBK)|(GABON AIRLINES)|(MCA Airlines)|(MCA)|(CALSON)|(Maldivo Airlines)|(ML)|(MAV)|(Maldivo)|(Virgin Pacific)|(VH)|(VNP)|(Zest Air)|(Z2)|(N)|(Yangon Airways)|(HK)|(N)|(Hotel Kilo)|(Eastar Jet)|(ZE)|(ESR)|(Eastar)|(Jin Air)|(LJ)|(JNA)|(Jin Air)|(Wataniya Airways)|(KW1)|(Air Arabia Maroc)|(3O)|(N)|(Air Arabia)|(Baltic Air lines)|(B1)|(BA1)|(Baltic)|(Ciel Canadien)|(YC)|(YCC)|(Ciel)|(Canadian National Airways)|(CN)|(YCP)|(CaNational)|(Epic Holiday)|(Epic Holidays)|(FA)|(4AA)|(Epic)|(Indochina Airlines)|(AXC)|(Airspup)|(Air Comet Chile)|(3I)|(N)|(Line Blue)|(L8)|(LBL)|(Bluebird)|(FlyLAL Charters)|(LLC)|(Salzburg arrows)|(SZA)|(SZ)|(N)|(SZA)|(Texas Wings)|(TQ)|(TXW)|(TXW)|(Dennis Sky)|(Dennis Sky Holding)|(DH)|(DSY)|(DSY)|(Zz)|(ZZ)|(N)|(Atifly)|(A1)|(A1F)|(atifly)|(Aerolineas heredas santa maria)|(SZB)|(Ciao Air)|(N)|(Pal airlines)|(5P)|(N)|(CanXpress)|(C1)|(CA1)|(CAX)|(Sharp Airlines)|(SH)|(SHA)|(SHARP)|(CanXplorer)|(C2)|(CAP)|(World Experience Airline)|(WEA)|(W1)|(WE1)|(WEA)|(ALAK)|(J4)|(N)|(Air Choice One)|(3E)|(N)|(Tianjin Airlines)|(GCR)|(China United)|(KN)|(N)|(Locair)|(ZQ)|(LOC)|(LOCAIR)|(Safi Airlines)|(4Q)|(N)|(SeaPort Airlines)|(K5)|(SQH)|(SASQUATCH)|(Salmon Air)|(S6)|(N)|(Bobb Air Freight)|(N)|(Star1 Airlines)|(V9)|(HCW)|(Pelita)|(6D)|(N)|(Alaska Seaplane Service)|(J5)|(N)|(Enerjet)|(ENJ)|(ENERJET AIR)|(MexicanaLink)|(I6)|(MXI)|(LINK)|(Island Spirit)|(IP)|(ISX)|(TACA Peru)|(T0)|(N)|(TACA PERU)|(Orbest)|(OBS)|(ORBEST)|(Southern Air Charter)|(SOA)|(SVG Air)|(SVG)|(Grenadines)|(Air Century)|(CEY)|(Pan Am World Airways Dominicana)|(PAWA Dominicana)|(7Q)|(N)|(PAWA)|(Primera Air)|(PF)|(N)|(PRIMERA)|(Air Antilles Express)|(3S)|(N)|(GREEN BIRD)|(Sol Lineas Aereas)|(OLS)|(FLIGHT SOL)|(Regional Paraguaya)|(P7)|(REP)|(REGIOPAR)|(VIP Ecuador)|(V6)|(N)|(Transportes Aereos Cielos Andinos)|(NDN)|(ANDINOS)|(Peruvian Airlines)|(P9)|(N)|(EasyFly)|(EFY)|(EASYFLY)|(Catovair)|(OC)|(N)|(CATOVAIR)|(Andalus Lineas Aereas)|(ANU)|(Andalus)|(Air 26)|(DCD)|(DUCARD)|(Mauritania Airways)|(MTW)|(MAURITANIA AIRWAYS)|(CEIBA Intercontinental)|(CEL)|(CEIBA LINE)|(Halcyonair)|(7Z)|(N)|(CREOLE)|(Zambia Skyways)|(K8)|(N)|(ZAMBIA SKIES)|(AlMasria Universal Airlines)|(UJ)|(LMU)|(ALMASRIA)|(EgyptAir Express)|(MSE)|(EGYPTAIR EXPRESS)|(SmartLynx Airlines)|(6Y)|(N)|(Air Italy Egypt)|(EUD)|(KoralBlue Airlines)|(K7)|(KBR)|(KORAL BLUE)|(Wind Rose Aviation)|(WRC)|(WIND ROSE)|(Elysian Airlines)|(E4)|(GIE)|(Sevenair)|(SEN)|(SEVENAIR)|(Hellenic Imperial Airways)|(HT)|(IMP)|(IMPERIAL)|(Amsterdam Airlines)|(WD)|(AAN)|(AMSTEL)|(Arik Niger)|(Q9)|(NAK)|(Dana Air)|(DA)|(N)|(DANACO)|(STP Airways)|(8F)|(STP)|(SAOTOME AIRWAYS)|(Med Airways)|(7Y)|(N)|(FLYING CARPET)|(Skyjet Airlines)|(UQ)|(SJU)|(SKYJET)|(Air Volga)|(G6)|(N)|(GOUMRAK)|(Transavia Denmark)|(TDK)|(Royal Falcon)|(RL)|(RFJ)|(Turkuaz Airlines)|(TRK)|(TURKU)|(Athens Airways)|(ZF)|(N)|(ATHENSAIR)|(Viking Hellas)|(VQ)|(VKH)|(DELPHI)|(Norlandair)|(FNA)|(NORLAND)|(Flugfelag Vestmannaeyja)|(FVM)|(ELEGANT)|(Lugansk Airlines)|(L7)|(N)|(ENTERPRISE LUHANSK)|(Gryphon Airlines)|(6P)|(N)|(Gadair European Airlines)|(GP)|(GDR)|(GADAIR)|(Spirit of Manila Airlines)|(SM)|(MNP)|(MANILA SKY)|(Chongqing Airlines)|(OQ)|(CQN)|(CHONG QING)|(Grand China Air)|(GDC)|(GRAND CHINA)|(West Air China)|(PN)|(CHB)|(WEST CHINA)|(QatXpress)|(qatXpress)|(C3)|(QAX)|(OneChina)|(OneChina)|(1C)|(1CH)|(NordStar Airlines)|(Y7)|(N)|(Joy Air)|(JR)|(JOY)|(JOY AIR)|(Air India Regional)|(CD)|(N)|(ALLIED)|(MDLR Airlines)|(9H)|(N)|(MDLR)|(Jagson Airlines)|(JGN)|(JAGSON)|(Maldivian)|(Q2)|(N)|(ISLAND AVIATION)|(Xpressair)|(XN)|(N)|(Strategic Airlines)|(VC)|(N)|(Fars Air Qeshm)|(QFZ)|(FARS AIR)|(Eastok Avia)|(EAA)|(Jupiter Airlines)|(JPU)|(JUPITERAIR)|(Vision Air International)|(VIS)|(Fuji Dream Airlines)|(JH)|(N)|(FUJI DREAM)|(Korea Express Air)|(KEA)|(Eznis Airways)|(EZA)|(EZNIS)|(Pacific Flier)|(PFL)|(KOROR)|(Syrian Pearl Airlines)|(PSB)|(SGA Airlines)|(5E)|(N)|(SIAM)|(Air2there)|(F8)|(N)|(EuropeSky)|(ES)|(EUV)|(EuropeSky)|(BRAZIL AIR)|(BRAZIL AIR)|(GB)|(BZE)|(BRAZIL AIR)|(Homer Air)|(Homer Sky)|(MR)|(OME)|(Jayrow)|(Wilderness Air)|(N)|(Whitaker Air)|(N)|(PanAm World Airways)|(WQ)|(PQW)|(Virginwings)|(YY)|(VWA)|(KSY)|(Kreta Sky)|(KY)|(KSY)|(KSY)|(SOCHI AIR)|(SOCHI)|(CQ)|(KOL)|(SLOW FROG)|(Wizz Air Ukraine)|(WU)|(WAU)|(WIZZAIR UKRAINE)|(88)|(VVN)|(LCM AIRLINES)|(LQ)|(LMM)|(Royal European Airlines)|(N)|(LSM Airlines)|(slowbird)|(PQ)|(LOO)|(slowbird)|(Zapolyarie Airlines)|(Zapolyarye Airlines)|(PZY)|(Finlandian)|(FN1)|(LionXpress)|(lionXpress)|(C4)|(LIX)|(LIX)|(Genesis)|(GK)|(N)|(Fly Dubai)|(FZ)|(FDB)|(Domenican Airlines)|(Domenican)|(D1)|(MDO)|(Domenican)|(ConneX European Airline)|(2CO)|(ConneX)|(Aereonautica militare)|(JY)|(AXZ)|(Kal Star Aviation)|(KLS)|(Huaxia)|(HUAXIA)|(G5)|(N)|(Zabaykalskii Airlines)|(Baikal Airlines)|(ZP)|(ZZZ)|(Lakeair)|(CBM America)|(XBM)|(AIRMAX)|(Marysya Airlines)|(MARYSYA AIRLINES)|(M4)|(1QA)|(MARSHAK AIR)|(N1)|(N1)|(N)|(Westfalia Express VA)|(WFX)|(JobAir)|(3B)|(N)|(Black Stallion Airways)|(BZ)|(BSA)|(Stallion)|(German International Air Lines)|(Germanair)|(GM)|(GER)|(TrasBrasil)|(TB)|(TBZ)|(TransBrasil Airlines)|(TH)|(THS)|(China SSS)|(Chunqiu Airlines)|(9C)|(N)|(AIR INDOCHINE)|(IIA)|(Happy Air)|(HPY)|(Solar Air)|(SRB)|(Solar Air)|(Air Mekong)|(P8)|(MKG)|(Air Mekong)|(ZABAIKAL AIRLINES)|(ZABAIKAL )|(Z6)|(ZTT)|(BAIKAL )|(TransHolding)|(Trans)|(TI)|(THI)|(SUR Lineas Aereas)|(SZZ)|(Aerolineas Africanas)|(AA1)|(Yeti Airways)|(YT)|(N)|(Yellowstone Club Private Shuttle)|(Y1)|(N)|(YCS)|(Caucasus Airlines)|(NS)|(N)|(Serbian Airlines)|(S1)|(SA1)|(Windward Islands Airways)|(WM)|(N)|(Winair)|(TransHolding System)|(YO)|(TYS)|(CCML Airlines)|(CB)|(CCC)|(Small Planet Airlines)|(ELC)|(Fly Brasil)|(Fly Brasil)|(F1)|(FBL)|(FBL)|(Trans Pas Air)|(T6)|(TP6)|(Himalayan Airlines)|(Himalaya)|(HC)|(HYM)|(Himalayan)|(Indya Airline Group)|(Indya1)|(G1)|(IG1)|(Indya1)|(Sunwing)|(WG)|(N)|(sunwing)|(Turkish Wings Domestic)|(TWD)|(TWD)|(Japan Regio)|(ZX)|(ZXY)|(OCEAN AIR CARGO)|(IXO)|(Norte Lineas Aereas)|(NORTE)|(N0)|(N)|(Austral Brasil)|(Austral Brasil lineas aereas)|(W7)|(N)|(Sky Regional)|(Air Canada Express)|(RS)|(N)|(Sky Regional)|(Baikotovitchestrian Airlines )|(BU)|(BUU)|(Luchsh Airlines )|(Air luch)|(L4)|(LJJ)|(russian sky)|(ENTERair)|(QQQ)|(Air Cargo Germany)|(6U)|(N)|(Loadmaster)|(Tway Airlines)|(TW)|(TWB)|(TWAY AIR)|(Papillon Grand Canyon Helicopters)|(HI)|(N)|(Jusur airways)|(JX)|(JSR)|(NEXT Brasil)|(NEXT)|(XB)|(NXB)|(XB)|(AeroWorld )|(Sovet Air )|(W4)|(WER)|(sovet)|(GNB Linhas Aereas)|(GN)|(N)|(Usa Sky Cargo)|(USky)|(E1)|(ES2)|(USKY)|(Hankook Airline)|(HN)|(HNX)|(HNX)|(REDjet)|(Z7)|(N)|(Red Jet Andes)|(PT)|(N)|(Red Jet Canada)|(QY)|(N)|(Red Jet Mexico)|(4X)|(N)|(Marusya Airways)|(Marusya Air)|(Y8)|(MRS)|(snowball)|(Era Alaska)|(7H)|(ERR)|(ERAH)|(AirRussia)|(RussianConector)|(R8)|(RRJ)|(russiancloud)|(Hankook Air US)|(H1)|(HA1)|(Carpatair Flight Training)|(SMW)|(Smartwings)|(Whitejets)|(WTJ)|(WHITEJET)|(VickJet)|(KT)|(VKJ)|(Vickjet)|(BVI Airways)|(XV)|(N)|(Hamburg Airways)|(HAY)|(Kan Air)|(KND)|(Kan Air)|(Air Cudlua)|(Air Cudlua)|(CUD)|(Cudlua)|(Air Explore)|(AXE)|(12 North)|(N12)|(12N)|(Holidays Czech Airlines)|(HCC)|(Comtel Air)|(COE)|(Mint Airways)|(MIC)|(Orbit Airlines)|(Orbit)|(OBT)|(Orbit)|(Air Bucharest)|(BUR)|(AlbaStar)|(LAV)|(Mauritania Airlines International)|(L6)|(MAI)|(MAT Airways)|(6F)|(MKD)|(Asian Wings Airways)|(AW)|(AWM)|(Asian Star)|(Air Arabia Egypt)|(E5)|(RBG)|(Eagles Airlines)|(EGS)|(EAGLES)|(YES Airways)|(YEP)|(Alitalia Cityliner)|(CT)|(N)|(Direct Aero Services)|(DSV)|(Medallion Air)|(MDP)|(MEDALS)|(Orchid Airlines)|(OI)|(ORC)|(Asia Wings)|(Y5)|(AWA)|(Air Batumi)|(BTM)|(Skywest Australia)|(XR)|(N)|(Nile Air)|(NP)|(NIA)|(NILEBIRD)|(Feeder Airlines)|(FDD)|(Senegal Airlines)|(DN)|(SGG)|(Fly 6ix)|(6I)|(N)|(Starbow Airlines)|(S9)|(N)|(Copenhagen Express)|(0X)|(CX0)|(Copex)|(BusinessAir)|(8B)|(BCC)|(SENIC AIRLINES)|(YR)|(N)|(XOJET)|(XOJ)|(Sky Wing Pacific)|(C7)|(CR7)|(Bateleur Air)|(BEU)|(Air Indus)|(Indus Airlines Pak)|(PP)|(AI0)|(AIPL)|(Orbit International Airlines)|(OAI)|(OA)|(Orbit Regional Airlines)|(OAR)|(OA)|(Orbit Atlantic Airways)|(OAN)|(Volotea)|(VOO)|(Volotea)|(Peach Aviation)|(MM)|(N)|(Air Peach)|(Russia State Transport)|(Federal State Budget Inst)|(RSD)|(STATE AERO)|(Malaysia Wings)|(MWI)|(MWI)|(Aviabus)|(U1)|(ABI)|(Michael Airlines)|(Javi)|(DF)|(MJG)|(MJG)|(Indonesia Sky)|(I5)|(IDS)|(Aws express)|(B0)|(aws)|(Southjet)|(SJS)|(Southjet connect)|(ZCS)|(Southjet cargo)|(XAN)|(Iberia Express)|(I2)|(IBS)|(AirOnix)|(OG)|(N)|(Nordic Global Airlines)|(NJ)|(NGB)|(Nordic Global)|(Scoot)|(TZ)|(SCO)|(Zenith International Airline)|(Zenith)|(ZN)|(ZNA)|(ZENITH)|(Orbit Airlines Azerbaijan)|(Orbit Azerbaijan)|(O1)|(OAB)|(Orbitaz)|(Flying kangaroo Airline)|(Skippy)|(FKA)|(Skippy)|(RusJet)|(RSJ)|(VietJet Air)|(VietJet)|(VJC)|(VIETJETAIR)|(Patriot Airways)|(P4)|(N)|(BQB Lineas Aereas)|(Buquebus)|(5Q)|(N)|(AirAsia Japan)|(WAJ)|(WING ASIA)|(Yellowtail)|(YE)|(YEL)|(Executive AirShare)|(XSR)|(Hebei Airlines)|(HBH)|(Hebei Air)|(Air KBZ)|(KBZ)|(Air KBZ)|(SkyWork Airlines )|(SX)|(N)|(SKYFOX)|(Maastricht Airlines)|(W2)|(N)|(Euro Jet)|(N)|(Ukraine Atlantic)|(UAT)|(Nesma Airlines)|(NMA)|(Nesma Airlines)|(East Horizon)|(EHN)|(EAST HORIZON)|(Air Majoro)|(MJP)|(Air Majoro)|(Rotana Jet)|(RJD)|(ROTANA)|(SOCHI AIR CHATER)|(Sochi Air )|(Q3)|(QER)|(russian doll)|(Denim Air )|(FlyNonstop)|(J7)|(N)|(DNM)|(Malindo Air)|(OD)|(MXD)|(Malindo)|(Hermes Airlines)|(HRM)|(HERMES)|(Flightlink Tanzania)|(Flightlink)|(Z9)|(N)|(IzAvia)|(I8)|(N)|(Maryland Air)|(Maryland)|(M1)|(M1F)|(Maryland Flight)|(VivaColombia)|(5Z)|(VVC)|(Flybe Finland Oy)|(FCM)|(FINNCOMM)|(Bingo Airways)|(Bingo)|(BGY)|(Apache Air)|(Apache)|(ZM)|(IWA)|(APACHE)|(MHS Aviation GmbH)|(M2)|(N)|(Jettor Airlines)|(Jettor)|(NR)|(JTO)|(JETHAPPY)|(Thai Lion Air)|(SL)|(N)|(Golden Myanmar Airlines)|(GMR)|(Golden Myanmar)|(Canaryfly)|(CNF)|(Sunrise Airways)|(KSZ)|(National Air Cargo)|(N8)|(NCR)|(Eastern Atlantic Virtual Airlines)|(EAV)|(EAVA)|(Citilink Indonesia)|(QG)|(N)|(SUPERGREEN)|(Transair)|(TTZ))";
		regexFlight += "( Flight:?)? ?[0-9]{1,4}( |•Ö)";
	}
	
	
	public void analyze(PdfContent content)
	{
		pdfContent = content;
		adjustedHeights = new ArrayList<Float>();
		adjustedHeights.add(0.0f);
		for (int i = 0; i < content.nPages; ++i)
		{
			float h = adjustedHeights.get(adjustedHeights.size() - 1);
			adjustedHeights.add(h + content.pageHeights.get(i) - INTERPAGE_MARGIN);
		}
		
		airlines = findMatches(content, regexFlight);
		dates = findMatches(content, regexDate);
		times = findMatches(content, regexTime);
		locations = findLocations(content, airports);
		trips = createTrips(airlines, dates, times, locations);
	}
	
	
	private static ArrayList<PdfWord> findLocations(PdfContent content, ArrayList<Airport> airports)
	{
		ArrayList<PdfWord> locations = new ArrayList<PdfWord>();
		for (Airport airport : airports)
		{
			String regex = airport.iata + "|" + airport.icao;
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(content.text);
			
			while (m.find())
			{
				PdfWord word = null;
				int idxFirst = m.start();
				int idxLast = m.end() - 1;
				
				// Don't consider matches with any letters in the front or back.
				if (idxFirst > 0)
				{
					if (Character.isLetter(content.text.charAt(idxFirst - 1)) ||
						Character.isLetter(content.text.charAt(idxLast + 1)))
					{
						continue;
					}
				}
				
				word = new PdfWord();
				word.text = m.group();
				word.setBoundingBox(content.charsX1.get(idxFirst), content.charsY1.get(idxFirst),
									content.charsX2.get(idxLast), content.charsY2.get(idxLast));
				word.page = content.getPageNumber(idxFirst) - 1;
				word.y2 = adjustedHeights.get(content.getPageNumber(idxFirst) - 1) + word.y;
				
				locations.add(word);
			}
		}
		return locations;
	}
	
	
	private ArrayList<PdfWord> findMatches(PdfContent content, String regex)
	{
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content.text);
		ArrayList<PdfWord> matchedWords = new ArrayList<PdfWord>();
		
		while (m.find())
		{
			PdfWord word = null;
			int idx = m.start();
			
			word = new PdfWord();
			word.text = m.group();
			word.x = content.charsX.get(idx);
			word.y = content.charsY.get(idx);
			word.y2 = adjustedHeights.get(content.getPageNumber(idx) - 1) + word.y;
		    
		    matchedWords.add(word);
		}
		
		return matchedWords;
	}
	
	
	private static ArrayList<TripDraft> createTrips(List<PdfWord> airlines, List<PdfWord> dates, List<PdfWord> times, List<PdfWord> airports)
	{
		ArrayList<TripDraft> trips = new ArrayList<TripDraft>();
		
		for (PdfWord airline : airlines)
		{
			TripDraft trip = new TripDraft();
			List<PdfWord> correspondingTimes = findCorrespondingTimes(airline, times);
			
			trip.airline = airline;
			trip.departureTime = correspondingTimes.get(0);
			trip.arrivalTime = correspondingTimes.get(1);
			trip.departureDate = findCorrespondingDate(trip.departureTime, dates);
			trip.arrivalDate = findCorrespondingDate(trip.arrivalTime, dates);
			trip.origin = findCorrespondingLocation(trip.departureTime, airports);
			trip.destination = findCorrespondingLocation(trip.arrivalTime, airports);
			
			trips.add(trip);
		}
		
		
		return trips;
	}
	
	
	// Return PdfWord with minimum y difference (greater than 0)
	private static List<PdfWord> findCorrespondingTimes(PdfWord flight, List<PdfWord> times)
	{
		float minDeltaY1 = Float.MAX_VALUE;
		float minDeltaY2 = Float.MAX_VALUE;
		PdfWord departure = null;
		PdfWord arrival = null;
		
		// Find two words (times) with minimum y difference with flight
		for (PdfWord time : times)
		{
			float deltaY = time.y2 - flight.y2;
			if (deltaY < -25.0f) continue;
			
			// Time closer to flight is departure (since departure is listed first).
			if (minDeltaY1 > deltaY)
			{
				minDeltaY1 = deltaY;
				departure = time;
				continue;
			}
			
			if (minDeltaY2 > deltaY)
			{
				minDeltaY2 = deltaY;
				arrival = time;
			}
		}
		
		return Arrays.asList(departure, arrival);
	}
	
	
	private static PdfWord findCorrespondingDate(PdfWord time, List<PdfWord> dates)
	{
		if (time == null) return null;
		float minDelta = Float.MAX_VALUE;
		float minDeltaY = Float.MAX_VALUE;
		PdfWord correspondingDate1 = null;
		PdfWord correspondingDate2 = null;
		
		for (PdfWord date : dates)
		{
			float deltaX = time.x - date.x;
			float deltaY = time.y2 - date.y2;
			float delta = deltaX * deltaX + deltaY * deltaY;
			
			if (minDelta > delta)
			{
				minDelta = delta;
				correspondingDate1 = date;
			}
			
			if (minDeltaY > deltaY && deltaY > 0)
			{
				minDeltaY = deltaY;
				correspondingDate2 = date;
			}
		}
		
		if (minDelta < 70*70) return correspondingDate1;
		return correspondingDate2;
	}
	
	
	private static PdfWord findCorrespondingLocation(PdfWord time, List<PdfWord> airports)
	{
		if (time == null) return null;
		float minDelta = Float.MAX_VALUE;
		PdfWord correspondingAirport = null;
		
		for (PdfWord airport : airports)
		{
			float deltaX = time.x - airport.x;
			float deltaY = time.y2 - airport.y2;
			float delta = deltaX * deltaX + deltaY * deltaY;
			
			if (minDelta > delta)
			{
				minDelta = delta;
				correspondingAirport = airport;
			}
		}
		
		return correspondingAirport;
	}
	
	// Convert TripDraft to Trip.
	public List<Trip> getTrips()
	{
		ArrayList<Trip> finalTrips = new ArrayList<Trip>();
		
		for (TripDraft trip : trips)
		{
			Trip t = new Trip();
			
			t.setAirline(trip.getAirline());
			t.setDepartureTime(trip.getDepartureTime());
			t.setDepartureDate(trip.getDepartureDate());
			t.setArrivalTime(trip.getArrivalTime());
			t.setArrivalDate(trip.getArrivalDate());
			t.setOrigin(trip.getOrigin());
			t.setDestination(trip.getDestination());
			
			finalTrips.add(t);
		}
		
		return finalTrips;
	}
	
	
	public void dispDebugInfo()
	{
		for (TripDraft trip : trips)
		{
			System.out.println(trip);
		}
	}
}
