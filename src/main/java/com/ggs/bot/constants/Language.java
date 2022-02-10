package com.ggs.bot.constants;

import java.util.Arrays;
import java.util.Optional;

public enum Language {

    AFRIKAANS("af","Afrikaans"),
    ALBANIAN("sq","Albanian"),
    AMHARIC("am","Amharic"),
    ARABIC("ar","Arabic"),
    ARMENIAN("hy","Armenian"),
    AZERBAIJANI("az","Azerbaijani"),
    BASQUE("eu","Basque"),
    BELARUSIAN("be","Belarusian"),
    BENGALI("bn","Bengali"),
    BOSNIAN("bs","Bosnian"),
    BULGARIAN("bg","Bulgarian"),
    CATALAN("ca","Catalan"),
    CEBUANO("ceb","Cebuano"),
    CHINESE_SIMPLIFIED("zh-CN","Chinese(Simplified)"),
    CHINESE_TRADITIONAL("zh-TW","Chinese(Traditional)"),
    CORSICAN("co","Corsican"),
    CROATIAN("hr","Croatian"),
    CZECH("cs","Czech"),
    DANISH("da","Danish"),
    DUTCH("nl","Dutch"),
    ENGLISH("en","English"),
    ESPERANTO("eo","Esperanto"),
    ESTONIAN("et","Estonian"),
    FINNISH("fi","Finnish"),
    FRENCH("fr","French"),
    FRISIAN("fy","Frisian"),
    GALICIAN("gl","Galician"),
    GEORGIAN("ka","Georgian"),
    GERMAN("de","German"),
    GREEK("el","Greek"),
    GUJARATI("gu","Gujarati"),
    HAITIANCREOLE("ht","HaitianCreole"),
    HAUSA("ha","Hausa"),
    HAWAIIAN("haw(ISO-639-2)","Hawaiian"),
    HEBREW("heoriw","Hebrew"),
    HINDI("hi","Hindi"),
    HMONG("hmn(ISO-639-2)","Hmong"),
    HUNGARIAN("hu","Hungarian"),
    ICELANDIC("is","Icelandic"),
    IGBO("ig","Igbo"),
    INDONESIAN("id","Indonesian"),
    IRISH("ga","Irish"),
    ITALIAN("it","Italian"),
    JAPANESE("ja","Japanese"),
    JAVANESE("jv","Javanese"),
    KANNADA("kn","Kannada"),
    KAZAKH("kk","Kazakh"),
    KHMER("km","Khmer"),
    KINYARWANDA("rw","Kinyarwanda"),
    KOREAN("ko","Korean"),
    KURDISH("ku","Kurdish"),
    KYRGYZ("ky","Kyrgyz"),
    LAO("lo","Lao"),
    LATVIAN("lv","Latvian"),
    LITHUANIAN("lt","Lithuanian"),
    LUXEMBOURGISH("lb","Luxembourgish"),
    MACEDONIAN("mk","Macedonian"),
    MALAGASY("mg","Malagasy"),
    MALAY("ms","Malay"),
    MALAYALAM("ml","Malayalam"),
    MALTESE("mt","Maltese"),
    MAORI("mi","Maori"),
    MARATHI("mr","Marathi"),
    MONGOLIAN("mn","Mongolian"),
    MYANMAR_BURMESE("my","Myanmar(Burmese)"),
    NEPALI("ne","Nepali"),
    NORWEGIAN("no","Norwegian"),
    NYANJA_CHICHEWA("ny","Nyanja(Chichewa)"),
    ODIA_ORIYA("or","Odia(Oriya)"),
    PASHTO("ps","Pashto"),
    PERSIAN("fa","Persian"),
    POLISH("pl","Polish"),
    PORTUGUESE_PORTUGAL_BRAZIL("pt","Portuguese(Portugal,Brazil)"),
    PUNJABI("pa","Punjabi"),
    ROMANIAN("ro","Romanian"),
    RUSSIAN("ru","Russian"),
    SAMOAN("sm","Samoan"),
    SCOTSGAELIC("gd","ScotsGaelic"),
    SERBIAN("sr","Serbian"),
    SESOTHO("st","Sesotho"),
    SHONA("sn","Shona"),
    SINDHI("sd","Sindhi"),
    SINHALA_SINHALESE("si","Sinhala(Sinhalese)"),
    SLOVAK("sk","Slovak"),
    SLOVENIAN("sl","Slovenian"),
    SOMALI("so","Somali"),
    SPANISH("es","Spanish"),
    SUNDANESE("su","Sundanese"),
    SWAHILI("sw","Swahili"),
    SWEDISH("sv","Swedish"),
    TAGALOG_FILIPINO("tl","Tagalog(Filipino)"),
    TAJIK("tg","Tajik"),
    TAMIL("ta","Tamil"),
    TATAR("tt","Tatar"),
    TELUGU("te","Telugu"),
    THAI("th","Thai"),
    TURKISH("tr","Turkish"),
    TURKMEN("tk","Turkmen"),
    UKRAINIAN("uk","Ukrainian"),
    URDU("ur","Urdu"),
    UYGHUR("ug","Uyghur"),
    UZBEK("uz","Uzbek"),
    VIETNAMESE("vi","Vietnamese"),
    WELSH("cy","Welsh"),
    XHOSA("xh","Xhosa"),
    YIDDISH("yi","Yiddish"),
    YORUBA("yo","Yoruba"),
    ZULU("zu","Zulu"),
    ;

    private final String code;
    private final String label;

    private Language(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static Optional<Language> getCode(String label) {
        return Arrays.stream(values()).filter(language -> language.getLabel().equalsIgnoreCase(label))
            .findFirst();
    }

    public static Optional<Language> getLabel(String code) {
        return Arrays.stream(values()).filter(language -> language.getCode().equalsIgnoreCase(code))
            .findFirst();
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

}
