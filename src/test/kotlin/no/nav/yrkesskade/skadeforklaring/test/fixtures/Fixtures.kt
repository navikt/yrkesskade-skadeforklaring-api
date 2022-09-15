package no.nav.yrkesskade.skadeforklaring.test.fixtures

import no.nav.yrkesskade.skadeforklaring.model.SkadeforklaringFactory
import no.nav.yrkesskade.skadeforklaring.model.SkadeforklaringFactory.Companion.medFoerteDinSkadeEllerSykdomTilFravaer
import no.nav.yrkesskade.skadeforklaring.model.SkadeforklaringFactory.Companion.medFravaerstype
import no.nav.yrkesskade.skadeforklaring.model.SkadeforklaringFactory.Companion.medInnmeldersNorskIdentitetsnummer
import no.nav.yrkesskade.skadeforklaring.model.SkadeforklaringFactory.Companion.medSkadelidtsNorskIdentitetsnummer

fun getEnkelskadeforklaring() = SkadeforklaringFactory.enSkadeforklaring()

fun getEnkelskadeforklaringUtenInnmeldersIdentitetsnummer() =
    SkadeforklaringFactory.enSkadeforklaring().medInnmeldersNorskIdentitetsnummer("")

fun getEnkelskadeforklaringUtenSkadelidtsIdentitetsnummer() =
    SkadeforklaringFactory.enSkadeforklaring(null).medSkadelidtsNorskIdentitetsnummer("")

fun getEnkelskadeforklaringMedHelsepersonellOppsoekt() = SkadeforklaringFactory.enSkadeforklaring()

fun getEnkelskadeforklaringUgyldigFravaertype() =
    SkadeforklaringFactory.enSkadeforklaring()
        .medFoerteDinSkadeEllerSykdomTilFravaer("merEnnTreDager")
        .medFravaerstype("denne finnes ikke")
fun getEnkelskadeforklaringIngenFravaer() =
    SkadeforklaringFactory.enSkadeforklaring().medFoerteDinSkadeEllerSykdomTilFravaer("nei")

fun getEnkelskadeforklaringOenskerIkkeOppgiFravaer() =
    SkadeforklaringFactory.enSkadeforklaring().medFoerteDinSkadeEllerSykdomTilFravaer("ikkeRelevant")
