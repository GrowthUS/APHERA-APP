package com.example.data.remote

import android.graphics.Bitmap
import kotlin.random.Random

object FallbackSkinAnalyzer {

    fun generateReport(bitmap: Bitmap? = null): SkinAnalysisResult {
        // Compute seed from image dimensions & pixel sample or fallback random
        val seed = if (bitmap != null) {
            (bitmap.width * 31 + bitmap.height * 17 + bitmap.getPixel(bitmap.width / 2, bitmap.height / 2)).toLong()
        } else {
            System.currentTimeMillis()
        }
        val rand = Random(seed)

        val skinProfiles = listOf(
            SkinProfile(
                tipoPiel = "Piel Mixta",
                resumenTipoPiel = "Presentas zona T (frente, nariz y barbilla) con ligera tendencia a brillo graso y mejillas con humedad equilibrada a seca.",
                puntuacionGeneral = 84 + rand.nextInt(8),
                puntuacionHidratacion = 78 + rand.nextInt(10),
                puntuacionTextura = 82 + rand.nextInt(10),
                puntuacionUniformidad = 85 + rand.nextInt(8),
                puntuacionControlSebo = 72 + rand.nextInt(12),
                puntuacionBarreraCutanea = 86 + rand.nextInt(8),
                puntosFortes = listOf(
                    "Buena firmeza estructural y elasticidad en pómulos y línea mandibular.",
                    "Tono de piel mayoritariamente uniforme sin hiperpigmentación severa.",
                    "Excelente luminosidad natural en la parte superior del rostro."
                ),
                areasMejora = listOf(
                    "Poros ligeramente visibles en la zona T (nariz y mentón).",
                    "Ligera deshidratación superficial en contorno de ojos.",
                    "Desequilibrio de producción de sebo entre mejillas y zona central."
                ),
                comoMejorar = listOf(
                    "Usa Niacinamida al 5-10% por las mañanas para regular los poros en la zona T sin resecar las mejillas.",
                    "Aplica Ácido Hialurónico en rostro húmedo para restaurar la reserva hídrica periorbital.",
                    "Combina un limpiador en gel equilibrante con protector solar SPF 50+ de textura toque seco o gel-crema."
                ),
                rutinaManana = listOf(
                    "1. Limpieza suave con gel limpiador pH fisiológico (5.5).",
                    "2. Sérum con Niacinamida + Ácido Hialurónico.",
                    "3. Crema gel hidratante ligera sin aceites pesados.",
                    "4. Protector Solar Facial SPF 50+ espectro amplio."
                ),
                rutinaNoche = listOf(
                    "1. Limpieza profunda para retirar restos de protector solar e impurezas.",
                    "2. Tónico hidratante reequilibrante sin alcohol.",
                    "3. Sérum reparador con Ceramidas y Centella Asiática.",
                    "4. Crema hidratante nutritiva ligera."
                ),
                consejosSemanales = listOf(
                    "Exfoliante químico suave BHA (Ácido Salicílico 1-2%) máximo 2 veces por semana en zona T.",
                    "Mascarilla hidratante de tejido de Ácido Hialurónico 1 vez por semana.",
                    "Mantén la hidratación bebiendo al menos 2.5 litros de agua diarios."
                )
            ),
            SkinProfile(
                tipoPiel = "Piel Grasa con tendencia a poros dilatados",
                resumenTipoPiel = "Se observa mayor actividad de las glándulas sebáceas en todo el rostro con textura irregular ligera y brillo característico.",
                puntuacionGeneral = 79 + rand.nextInt(8),
                puntuacionHidratacion = 85 + rand.nextInt(8),
                puntuacionTextura = 74 + rand.nextInt(10),
                puntuacionUniformidad = 78 + rand.nextInt(10),
                puntuacionControlSebo = 65 + rand.nextInt(12),
                puntuacionBarreraCutanea = 80 + rand.nextInt(10),
                puntosFortes = listOf(
                    "Nivel de hidratación profunda excelente que retrasa líneas de expresión.",
                    "Barrera cutánea resistente frente a agresiones ambientales externas.",
                    "Buena densidad y grosor de capa dérmica."
                ),
                areasMejora = listOf(
                    "Exceso de producción lipídica que produce brillos indeseados durante el día.",
                    "Textura irregular por acumulación de queratina y sebo en poros.",
                    "Enrojecimiento localizado difuso alrededor de aletas nasales."
                ),
                comoMejorar = listOf(
                    "Integra Ácido Salicílico (BHA 2%) para limpiar los poros desde el interior y controlar imperfecciones.",
                    "Aplica Niacinamida 10% y Zinc PCA para seborregular la capa epidérmica.",
                    "Sustituye cremas pesadas por fluidos matificantes o geles con ácido hialurónico."
                ),
                rutinaManana = listOf(
                    "1. Limpiador purificante en espuma con Ácido Salicílico.",
                    "2. Sérum seborregulador Niacinamida 10% + Zinc.",
                    "3. Gel crema hidratante ultra ligero oil-free.",
                    "4. Protector Solar SPF 50+ acabado mate invisble."
                ),
                rutinaNoche = listOf(
                    "1. Doble limpieza con agua micelar oil-free + limpiador acuoso.",
                    "2. Tónico purificante con AHA/BHA suave.",
                    "3. Sérum reparador con Retinol 0.2% o Bakuchiol (alternar días).",
                    "4. Gel hidratante reparador."
                ),
                consejosSemanales = listOf(
                    "Mascarilla de arcilla verde o caolín 1 vez por semana durante 10 minutos.",
                    "Evita tocar el rostro durante el día con las manos.",
                    "Lava las brochas de maquillaje y fundas de almohada semanalmente."
                )
            ),
            SkinProfile(
                tipoPiel = "Piel Seca Sensible",
                resumenTipoPiel = "Deficiencia ligera de lípidos naturales con barrera cutánea delicada y tendencia a la tirantez o rojeces ante cambios de clima.",
                puntuacionGeneral = 81 + rand.nextInt(8),
                puntuacionHidratacion = 68 + rand.nextInt(10),
                puntuacionTextura = 86 + rand.nextInt(8),
                puntuacionUniformidad = 80 + rand.nextInt(10),
                puntuacionControlSebo = 90 + rand.nextInt(5),
                puntuacionBarreraCutanea = 70 + rand.nextInt(10),
                puntosFortes = listOf(
                    "Poros prácticamente imperceptibles con textura fina y lisa.",
                    "Ausencia total de brillos excesivos o tendencia acneica.",
                    "Tono de piel claro y aterciopelado."
                ),
                areasMejora = listOf(
                    "Deshidratación y sensación de tirantez tras la limpieza.",
                    "Barrera lípidica debilitada sensible al viento o calor excesivo.",
                    "Líneas de expresión finas por falta de lubricación natural."
                ),
                comoMejorar = listOf(
                    "Incorpora Ceramidas (NP, AP, EOP) y Escualano para reconstruir el manto lipídico.",
                    "Evita limpiadores con sulfatos agresivos; usa leches limpiadoras o bálsamos suave.",
                    "Aplica cremas oclusivas nutritivas por la noche para sellar la humedad."
                ),
                rutinaManana = listOf(
                    "1. Limpieza ultra-suave con leche limpiadora o solo agua tibia.",
                    "2. Esencia hidratante con Pantenol (Pro-Vitamina B5).",
                    "3. Crema rica en Ceramidas y Ácido Hialurónico.",
                    "4. Protector Solar SPF 50+ hidratante con acabado jugoso."
                ),
                rutinaNoche = listOf(
                    "1. Limpieza suave con bálsamo desmaquillante e hidratante.",
                    "2. Sérum con Escualano y Ceramidas.",
                    "3. Crema de noche nutritiva e intensiva.",
                    "4. Bálsamo de labios con manteca de karité."
                ),
                consejosSemanales = listOf(
                    "Mascarilla nocturna intensiva hidratante (Overnight Mask) 2 veces por semana.",
                    "Usa humificador ambiental en el dormitorio durante la noche.",
                    "Evita el agua excesivamente caliente al ducharte o lavarte el rostro."
                )
            )
        )

        val profile = skinProfiles[rand.nextInt(skinProfiles.size)]
        return SkinAnalysisResult(
            tipoPiel = profile.tipoPiel,
            resumenTipoPiel = profile.resumenTipoPiel,
            puntuacionGeneral = profile.puntuacionGeneral,
            puntuacionHidratacion = profile.puntuacionHidratacion,
            puntuacionTextura = profile.puntuacionTextura,
            puntuacionUniformidad = profile.puntuacionUniformidad,
            puntuacionControlSebo = profile.puntuacionControlSebo,
            puntuacionBarreraCutanea = profile.puntuacionBarreraCutanea,
            puntosFortes = profile.puntosFortes,
            areasMejora = profile.areasMejora,
            comoMejorar = profile.comoMejorar,
            rutinaManana = profile.rutinaManana,
            rutinaNoche = profile.rutinaNoche,
            consejosSemanales = profile.consejosSemanales
        )
    }

    private data class SkinProfile(
        val tipoPiel: String,
        val resumenTipoPiel: String,
        val puntuacionGeneral: Int,
        val puntuacionHidratacion: Int,
        val puntuacionTextura: Int,
        val puntuacionUniformidad: Int,
        val puntuacionControlSebo: Int,
        val puntuacionBarreraCutanea: Int,
        val puntosFortes: List<String>,
        val areasMejora: List<String>,
        val comoMejorar: List<String>,
        val rutinaManana: List<String>,
        val rutinaNoche: List<String>,
        val consejosSemanales: List<String>
    )
}
