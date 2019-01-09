package ch.leadrian.samp.kamp.core.api.constants

import ch.leadrian.samp.kamp.core.TextKeys
import ch.leadrian.samp.kamp.core.api.text.HasTextKey
import ch.leadrian.samp.kamp.core.api.text.TextKey
import org.apache.commons.collections4.trie.PatriciaTrie

/**
 * Taken from https://github.com/Shoebill/shoebill-api
 */
@Suppress("SpellCheckingInspection", "unused")
enum class VehicleModel(
        override val value: Int,
        override val textKey: TextKey,
        val modelName: String,
        val type: VehicleType,
        val numberOfSeats: Int
) : ConstantValue<Int>, HasTextKey {

    LANDSTALKER(
            value = SAMPConstants.VEHICLE_LANDSTALKER,
            textKey = TextKeys.vehicle.model.name.landstalker,
            modelName = "Landstalker",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    BRAVURA(
            value = SAMPConstants.VEHICLE_BRAVURA,
            textKey = TextKeys.vehicle.model.name.bravura,
            modelName = "Bravura",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    BUFFALO(
            value = SAMPConstants.VEHICLE_BUFFALO,
            textKey = TextKeys.vehicle.model.name.buffalo,
            modelName = "Buffalo",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    LINERUNNER(
            value = SAMPConstants.VEHICLE_LINERUNNER,
            textKey = TextKeys.vehicle.model.name.linerunner,
            modelName = "Linerunner",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    PERRENIAL(
            value = SAMPConstants.VEHICLE_PERRENIAL,
            textKey = TextKeys.vehicle.model.name.perrenial,
            modelName = "Perennial",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    SENTINEL(
            value = SAMPConstants.VEHICLE_SENTINEL,
            textKey = TextKeys.vehicle.model.name.sentinel,
            modelName = "Sentinel",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    DUMPER(
            value = SAMPConstants.VEHICLE_DUMPER,
            textKey = TextKeys.vehicle.model.name.dumper,
            modelName = "Dumper",
            type = VehicleType.CAR,
            numberOfSeats = 1
    ),
    FIRETRUCK(
            value = SAMPConstants.VEHICLE_FIRETRUCK,
            textKey = TextKeys.vehicle.model.name.firetruck,
            modelName = "Firetruck",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    TRASHMASTER(
            value = SAMPConstants.VEHICLE_TRASHMASTER,
            textKey = TextKeys.vehicle.model.name.trashmaster,
            modelName = "Trashmaster",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    STRETCH(
            value = SAMPConstants.VEHICLE_STRETCH,
            textKey = TextKeys.vehicle.model.name.stretch,
            modelName = "Stretch",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    MANANA(
            value = SAMPConstants.VEHICLE_MANANA,
            textKey = TextKeys.vehicle.model.name.manana,
            modelName = "Manana",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    INFERNUS(
            value = SAMPConstants.VEHICLE_INFERNUS,
            textKey = TextKeys.vehicle.model.name.infernus,
            modelName = "Infernus",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    VOODOO(
            value = SAMPConstants.VEHICLE_VOODOO,
            textKey = TextKeys.vehicle.model.name.voodoo,
            modelName = "Voodoo",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    PONY(
            value = SAMPConstants.VEHICLE_PONY,
            textKey = TextKeys.vehicle.model.name.pony,
            modelName = "Pony",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    MULE(
            value = SAMPConstants.VEHICLE_MULE,
            textKey = TextKeys.vehicle.model.name.mule,
            modelName = "Mule",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    CHEETAH(
            value = SAMPConstants.VEHICLE_CHEETAH,
            textKey = TextKeys.vehicle.model.name.cheetah,
            modelName = "Cheetah",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    AMBULANCE(
            value = SAMPConstants.VEHICLE_AMBULANCE,
            textKey = TextKeys.vehicle.model.name.ambulance,
            modelName = "Ambulance",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    LEVIATHAN(
            value = SAMPConstants.VEHICLE_LEVIATHAN,
            textKey = TextKeys.vehicle.model.name.leviathan,
            modelName = "Leviathan",
            type = VehicleType.HELICOPTER,
            numberOfSeats = 2
    ),
    MOONBEAM(
            value = SAMPConstants.VEHICLE_MOONBEAM,
            textKey = TextKeys.vehicle.model.name.moonbeam,
            modelName = "Moonbeam",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    ESPERANTO(
            value = SAMPConstants.VEHICLE_ESPERANTO,
            textKey = TextKeys.vehicle.model.name.esperanto,
            modelName = "Esperanto",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    TAXI(
            value = SAMPConstants.VEHICLE_TAXI,
            textKey = TextKeys.vehicle.model.name.taxi,
            modelName = "Taxi",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    WASHINGTON(
            value = SAMPConstants.VEHICLE_WASHINGTON,
            textKey = TextKeys.vehicle.model.name.washington,
            modelName = "Washington",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    BOBCAT(
            value = SAMPConstants.VEHICLE_BOBCAT,
            textKey = TextKeys.vehicle.model.name.bobcat,
            modelName = "Bobcat",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    MRWHOOPEE(
            value = SAMPConstants.VEHICLE_MRWHOOPEE,
            textKey = TextKeys.vehicle.model.name.mrwhoopee,
            modelName = "Mr Whoopee",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    BFINJECTION(
            value = SAMPConstants.VEHICLE_BFINJECTION,
            textKey = TextKeys.vehicle.model.name.bfinjection,
            modelName = "BF Injection",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    HUNTER(
            value = SAMPConstants.VEHICLE_HUNTER,
            textKey = TextKeys.vehicle.model.name.hunter,
            modelName = "Hunter",
            type = VehicleType.HELICOPTER,
            numberOfSeats = 1
    ),
    PREMIER(
            value = SAMPConstants.VEHICLE_PREMIER,
            textKey = TextKeys.vehicle.model.name.premier,
            modelName = "Premier",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    ENFORCER(
            value = SAMPConstants.VEHICLE_ENFORCER,
            textKey = TextKeys.vehicle.model.name.enforcer,
            modelName = "Enforcer",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    SECURICAR(
            value = SAMPConstants.VEHICLE_SECURICAR,
            textKey = TextKeys.vehicle.model.name.securicar,
            modelName = "Securicar",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    BANSHEE(
            value = SAMPConstants.VEHICLE_BANSHEE,
            textKey = TextKeys.vehicle.model.name.banshee,
            modelName = "Banshee",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    PREDATOR(
            value = SAMPConstants.VEHICLE_PREDATOR,
            textKey = TextKeys.vehicle.model.name.predator,
            modelName = "Predator",
            type = VehicleType.BOAT,
            numberOfSeats = 0
    ),
    BUS(
            value = SAMPConstants.VEHICLE_BUS,
            textKey = TextKeys.vehicle.model.name.bus,
            modelName = "Bus",
            type = VehicleType.CAR,
            numberOfSeats = 8
    ),
    RHINO(
            value = SAMPConstants.VEHICLE_RHINO,
            textKey = TextKeys.vehicle.model.name.rhino,
            modelName = "Rhino",
            type = VehicleType.TANK,
            numberOfSeats = 1
    ),
    BARRACKS(
            value = SAMPConstants.VEHICLE_BARRACKS,
            textKey = TextKeys.vehicle.model.name.barracks,
            modelName = "Barracks",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    HOTKNIFE(
            value = SAMPConstants.VEHICLE_HOTKNIFE,
            textKey = TextKeys.vehicle.model.name.hotknife,
            modelName = "Hotknife",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    ARTICLETRAILER1(
            value = SAMPConstants.VEHICLE_ARTICLETRAILER1,
            textKey = TextKeys.vehicle.model.name.articletrailer1,
            modelName = "Trailer",
            type = VehicleType.TRAILER,
            numberOfSeats = 0
    ),
    PREVION(
            value = SAMPConstants.VEHICLE_PREVION,
            textKey = TextKeys.vehicle.model.name.previon,
            modelName = "Previon",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    COACH(
            value = SAMPConstants.VEHICLE_COACH,
            textKey = TextKeys.vehicle.model.name.coach,
            modelName = "Coach",
            type = VehicleType.CAR,
            numberOfSeats = 8
    ),
    CABBIE(
            value = SAMPConstants.VEHICLE_CABBIE,
            textKey = TextKeys.vehicle.model.name.cabbie,
            modelName = "Cabbie",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    STALLION(
            value = SAMPConstants.VEHICLE_STALLION,
            textKey = TextKeys.vehicle.model.name.stallion,
            modelName = "Stallion",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    RUMPO(
            value = SAMPConstants.VEHICLE_RUMPO,
            textKey = TextKeys.vehicle.model.name.rumpo,
            modelName = "Rumpo",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    RCBANDIT(
            value = SAMPConstants.VEHICLE_RCBANDIT,
            textKey = TextKeys.vehicle.model.name.rcbandit,
            modelName = "RC Bandit",
            type = VehicleType.REMOTE_CONTROL,
            numberOfSeats = 1
    ),
    ROMERO(
            value = SAMPConstants.VEHICLE_ROMERO,
            textKey = TextKeys.vehicle.model.name.romero,
            modelName = "Romero",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    PACKER(
            value = SAMPConstants.VEHICLE_PACKER,
            textKey = TextKeys.vehicle.model.name.packer,
            modelName = "Packer",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    MONSTER(
            value = SAMPConstants.VEHICLE_MONSTER,
            textKey = TextKeys.vehicle.model.name.monster,
            modelName = "Monster",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    ADMIRAL(
            value = SAMPConstants.VEHICLE_ADMIRAL,
            textKey = TextKeys.vehicle.model.name.admiral,
            modelName = "Admiral",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    SQUALO(
            value = SAMPConstants.VEHICLE_SQUALO,
            textKey = TextKeys.vehicle.model.name.squalo,
            modelName = "Squalo",
            type = VehicleType.BOAT,
            numberOfSeats = 0
    ),
    SEASPARROW(
            value = SAMPConstants.VEHICLE_SEASPARROW,
            textKey = TextKeys.vehicle.model.name.seasparrow,
            modelName = "Seasparrow",
            type = VehicleType.HELICOPTER,
            numberOfSeats = 2
    ),
    PIZZABOY(
            value = SAMPConstants.VEHICLE_PIZZABOY,
            textKey = TextKeys.vehicle.model.name.pizzaboy,
            modelName = "Pizzaboy",
            type = VehicleType.MOTORBIKE,
            numberOfSeats = 1
    ),
    TRAM(
            value = SAMPConstants.VEHICLE_TRAM,
            textKey = TextKeys.vehicle.model.name.tram,
            modelName = "Tram",
            type = VehicleType.TRAIN,
            numberOfSeats = 4
    ),
    ARTICLETRAILER2(
            value = SAMPConstants.VEHICLE_ARTICLETRAILER2,
            textKey = TextKeys.vehicle.model.name.articletrailer2,
            modelName = "Trailer",
            type = VehicleType.TRAILER,
            numberOfSeats = 0
    ),
    TURISMO(
            value = SAMPConstants.VEHICLE_TURISMO,
            textKey = TextKeys.vehicle.model.name.turismo,
            modelName = "Turismo",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    SPEEDER(
            value = SAMPConstants.VEHICLE_SPEEDER,
            textKey = TextKeys.vehicle.model.name.speeder,
            modelName = "Speeder",
            type = VehicleType.BOAT,
            numberOfSeats = 0
    ),
    REEFER(
            value = SAMPConstants.VEHICLE_REEFER,
            textKey = TextKeys.vehicle.model.name.reefer,
            modelName = "Reefer",
            type = VehicleType.BOAT,
            numberOfSeats = 0
    ),
    TROPIC(
            value = SAMPConstants.VEHICLE_TROPIC,
            textKey = TextKeys.vehicle.model.name.tropic,
            modelName = "Tropic",
            type = VehicleType.BOAT,
            numberOfSeats = 0
    ),
    FLATBED(
            value = SAMPConstants.VEHICLE_FLATBED,
            textKey = TextKeys.vehicle.model.name.flatbed,
            modelName = "Flatbed",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    YANKEE(
            value = SAMPConstants.VEHICLE_YANKEE,
            textKey = TextKeys.vehicle.model.name.yankee,
            modelName = "Yankee",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    CADDY(
            value = SAMPConstants.VEHICLE_CADDY,
            textKey = TextKeys.vehicle.model.name.caddy,
            modelName = "Caddy",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    SOLAIR(
            value = SAMPConstants.VEHICLE_SOLAIR,
            textKey = TextKeys.vehicle.model.name.solair,
            modelName = "Solair",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    BERKLEYSRCVAN(
            value = SAMPConstants.VEHICLE_BERKLEYSRCVAN,
            textKey = TextKeys.vehicle.model.name.berkleysrcvan,
            modelName = "Berkleys RC Van",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    SKIMMER(
            value = SAMPConstants.VEHICLE_SKIMMER,
            textKey = TextKeys.vehicle.model.name.skimmer,
            modelName = "Skimmer",
            type = VehicleType.AIRCRAFT,
            numberOfSeats = 2
    ),
    PCJ600(
            value = SAMPConstants.VEHICLE_PCJ600,
            textKey = TextKeys.vehicle.model.name.pcj600,
            modelName = "PCJ-600",
            type = VehicleType.MOTORBIKE,
            numberOfSeats = 2
    ),
    FAGGIO(
            value = SAMPConstants.VEHICLE_FAGGIO,
            textKey = TextKeys.vehicle.model.name.faggio,
            modelName = "Faggio",
            type = VehicleType.MOTORBIKE,
            numberOfSeats = 2
    ),
    FREEWAY(
            value = SAMPConstants.VEHICLE_FREEWAY,
            textKey = TextKeys.vehicle.model.name.freeway,
            modelName = "Freeway",
            type = VehicleType.MOTORBIKE,
            numberOfSeats = 0
    ),
    RCBARON(
            value = SAMPConstants.VEHICLE_RCBARON,
            textKey = TextKeys.vehicle.model.name.rcbaron,
            modelName = "RC Baron",
            type = VehicleType.REMOTE_CONTROL,
            numberOfSeats = 1
    ),
    RCRAIDER(
            value = SAMPConstants.VEHICLE_RCRAIDER,
            textKey = TextKeys.vehicle.model.name.rcraider,
            modelName = "RC Raider",
            type = VehicleType.REMOTE_CONTROL,
            numberOfSeats = 1
    ),
    GLENDALE(
            value = SAMPConstants.VEHICLE_GLENDALE,
            textKey = TextKeys.vehicle.model.name.glendale,
            modelName = "Glendale",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    OCEANIC(
            value = SAMPConstants.VEHICLE_OCEANIC,
            textKey = TextKeys.vehicle.model.name.oceanic,
            modelName = "Oceanic",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    SANCHEZ(
            value = SAMPConstants.VEHICLE_SANCHEZ,
            textKey = TextKeys.vehicle.model.name.sanchez,
            modelName = "Sanchez",
            type = VehicleType.MOTORBIKE,
            numberOfSeats = 2
    ),
    SPARROW(
            value = SAMPConstants.VEHICLE_SPARROW,
            textKey = TextKeys.vehicle.model.name.sparrow,
            modelName = "Sparrow",
            type = VehicleType.HELICOPTER,
            numberOfSeats = 2
    ),
    PATRIOT(
            value = SAMPConstants.VEHICLE_PATRIOT,
            textKey = TextKeys.vehicle.model.name.patriot,
            modelName = "Patriot",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    QUAD(
            value = SAMPConstants.VEHICLE_QUAD,
            textKey = TextKeys.vehicle.model.name.quad,
            modelName = "Quad",
            type = VehicleType.MOTORBIKE,
            numberOfSeats = 2
    ),
    COASTGUARD(
            value = SAMPConstants.VEHICLE_COASTGUARD,
            textKey = TextKeys.vehicle.model.name.coastguard,
            modelName = "Coastguard",
            type = VehicleType.BOAT,
            numberOfSeats = 0
    ),
    DINGHY(
            value = SAMPConstants.VEHICLE_DINGHY,
            textKey = TextKeys.vehicle.model.name.dinghy,
            modelName = "Dinghy",
            type = VehicleType.BOAT,
            numberOfSeats = 0
    ),
    HERMES(
            value = SAMPConstants.VEHICLE_HERMES,
            textKey = TextKeys.vehicle.model.name.hermes,
            modelName = "Hermes",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    SABRE(
            value = SAMPConstants.VEHICLE_SABRE,
            textKey = TextKeys.vehicle.model.name.sabre,
            modelName = "Sabre",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    RUSTLER(
            value = SAMPConstants.VEHICLE_RUSTLER,
            textKey = TextKeys.vehicle.model.name.rustler,
            modelName = "Rustler",
            type = VehicleType.AIRCRAFT,
            numberOfSeats = 1
    ),
    ZR350(
            value = SAMPConstants.VEHICLE_ZR350,
            textKey = TextKeys.vehicle.model.name.zr350,
            modelName = "ZR-350",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    WALTON(
            value = SAMPConstants.VEHICLE_WALTON,
            textKey = TextKeys.vehicle.model.name.walton,
            modelName = "Walton",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    REGINA(
            value = SAMPConstants.VEHICLE_REGINA,
            textKey = TextKeys.vehicle.model.name.regina,
            modelName = "Regina",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    COMET(
            value = SAMPConstants.VEHICLE_COMET,
            textKey = TextKeys.vehicle.model.name.comet,
            modelName = "Comet",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    BMX(
            value = SAMPConstants.VEHICLE_BMX,
            textKey = TextKeys.vehicle.model.name.bmx,
            modelName = "BMX",
            type = VehicleType.BICYCLE,
            numberOfSeats = 1
    ),
    BURRITO(
            value = SAMPConstants.VEHICLE_BURRITO,
            textKey = TextKeys.vehicle.model.name.burrito,
            modelName = "Burrito",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    CAMPER(
            value = SAMPConstants.VEHICLE_CAMPER,
            textKey = TextKeys.vehicle.model.name.camper,
            modelName = "Camper",
            type = VehicleType.CAR,
            numberOfSeats = 3
    ),
    MARQUIS(
            value = SAMPConstants.VEHICLE_MARQUIS,
            textKey = TextKeys.vehicle.model.name.marquis,
            modelName = "Marquis",
            type = VehicleType.BOAT,
            numberOfSeats = 0
    ),
    BAGGAGE(
            value = SAMPConstants.VEHICLE_BAGGAGE,
            textKey = TextKeys.vehicle.model.name.baggage,
            modelName = "Baggage",
            type = VehicleType.CAR,
            numberOfSeats = 1
    ),
    DOZER(
            value = SAMPConstants.VEHICLE_DOZER,
            textKey = TextKeys.vehicle.model.name.dozer,
            modelName = "Dozer",
            type = VehicleType.CAR,
            numberOfSeats = 1
    ),
    MAVERICK(
            value = SAMPConstants.VEHICLE_MAVERICK,
            textKey = TextKeys.vehicle.model.name.maverick,
            modelName = "Maverick",
            type = VehicleType.HELICOPTER,
            numberOfSeats = 4
    ),
    SANNEWSMAVERICK(
            value = SAMPConstants.VEHICLE_SANNEWSMAVERICK,
            textKey = TextKeys.vehicle.model.name.sannewsmaverick,
            modelName = "News Chopper",
            type = VehicleType.HELICOPTER,
            numberOfSeats = 2
    ),
    RANCHER(
            value = SAMPConstants.VEHICLE_RANCHER,
            textKey = TextKeys.vehicle.model.name.rancher,
            modelName = "Rancher",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    FBIRANCHER(
            value = SAMPConstants.VEHICLE_FBIRANCHER,
            textKey = TextKeys.vehicle.model.name.fbirancher,
            modelName = "FBI Rancher",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    VIRGO(
            value = SAMPConstants.VEHICLE_VIRGO,
            textKey = TextKeys.vehicle.model.name.virgo,
            modelName = "Virgo",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    GREENWOOD(
            value = SAMPConstants.VEHICLE_GREENWOOD,
            textKey = TextKeys.vehicle.model.name.greenwood,
            modelName = "Greenwood",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    JETMAX(
            value = SAMPConstants.VEHICLE_JETMAX,
            textKey = TextKeys.vehicle.model.name.jetmax,
            modelName = "Jetmax",
            type = VehicleType.BOAT,
            numberOfSeats = 0
    ),
    HOTRINGRACER(
            value = SAMPConstants.VEHICLE_HOTRINGRACER,
            textKey = TextKeys.vehicle.model.name.hotringracer,
            modelName = "Hotring",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    SANDKING(
            value = SAMPConstants.VEHICLE_SANDKING,
            textKey = TextKeys.vehicle.model.name.sandking,
            modelName = "Sandking",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    BLISTACOMPACT(
            value = SAMPConstants.VEHICLE_BLISTACOMPACT,
            textKey = TextKeys.vehicle.model.name.blistacompact,
            modelName = "Blista Compact",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    POLICEMAVERICK(
            value = SAMPConstants.VEHICLE_POLICEMAVERICK,
            textKey = TextKeys.vehicle.model.name.policemaverick,
            modelName = "Police Maverick",
            type = VehicleType.HELICOPTER,
            numberOfSeats = 4
    ),
    BOXVILLE(
            value = SAMPConstants.VEHICLE_BOXVILLE,
            textKey = TextKeys.vehicle.model.name.boxville,
            modelName = "Boxville",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    BENSON(
            value = SAMPConstants.VEHICLE_BENSON,
            textKey = TextKeys.vehicle.model.name.benson,
            modelName = "Benson",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    MESA(
            value = SAMPConstants.VEHICLE_MESA,
            textKey = TextKeys.vehicle.model.name.mesa,
            modelName = "Mesa",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    RCGOBLIN(
            value = SAMPConstants.VEHICLE_RCGOBLIN,
            textKey = TextKeys.vehicle.model.name.rcgoblin,
            modelName = "RC Goblin",
            type = VehicleType.REMOTE_CONTROL,
            numberOfSeats = 1
    ),
    HOTRINGRACERA(
            value = SAMPConstants.VEHICLE_HOTRINGRACERA,
            textKey = TextKeys.vehicle.model.name.hotringracera,
            modelName = "Hotring Racer",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    HOTRINGRACERB(
            value = SAMPConstants.VEHICLE_HOTRINGRACERB,
            textKey = TextKeys.vehicle.model.name.hotringracerb,
            modelName = "Hotring Racer",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    BLOODRINGBANGER(
            value = SAMPConstants.VEHICLE_BLOODRINGBANGER,
            textKey = TextKeys.vehicle.model.name.bloodringbanger,
            modelName = "Bloodring Banger",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    RANCHERLURE(
            value = SAMPConstants.VEHICLE_RANCHERLURE,
            textKey = TextKeys.vehicle.model.name.rancherlure,
            modelName = "Rancher",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    SUPERGT(
            value = SAMPConstants.VEHICLE_SUPERGT,
            textKey = TextKeys.vehicle.model.name.supergt,
            modelName = "Super GT",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    ELEGANT(
            value = SAMPConstants.VEHICLE_ELEGANT,
            textKey = TextKeys.vehicle.model.name.elegant,
            modelName = "Elegant",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    JOURNEY(
            value = SAMPConstants.VEHICLE_JOURNEY,
            textKey = TextKeys.vehicle.model.name.journey,
            modelName = "Journey",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    BIKE(
            value = SAMPConstants.VEHICLE_BIKE,
            textKey = TextKeys.vehicle.model.name.bike,
            modelName = "Bike",
            type = VehicleType.BICYCLE,
            numberOfSeats = 1
    ),
    MOUNTAINBIKE(
            value = SAMPConstants.VEHICLE_MOUNTAINBIKE,
            textKey = TextKeys.vehicle.model.name.mountainbike,
            modelName = "Mountain Bike",
            type = VehicleType.BICYCLE,
            numberOfSeats = 1
    ),
    BEAGLE(
            value = SAMPConstants.VEHICLE_BEAGLE,
            textKey = TextKeys.vehicle.model.name.beagle,
            modelName = "Beagle",
            type = VehicleType.AIRCRAFT,
            numberOfSeats = 2
    ),
    CROPDUST(
            value = SAMPConstants.VEHICLE_CROPDUST,
            textKey = TextKeys.vehicle.model.name.cropdust,
            modelName = "Cropdust",
            type = VehicleType.AIRCRAFT,
            numberOfSeats = 1
    ),
    STUNTPLANE(
            value = SAMPConstants.VEHICLE_STUNTPLANE,
            textKey = TextKeys.vehicle.model.name.stuntplane,
            modelName = "Stunt",
            type = VehicleType.AIRCRAFT,
            numberOfSeats = 1
    ),
    TANKER(
            value = SAMPConstants.VEHICLE_TANKER,
            textKey = TextKeys.vehicle.model.name.tanker,
            modelName = "Tanker",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    ROADTRAIN(
            value = SAMPConstants.VEHICLE_ROADTRAIN,
            textKey = TextKeys.vehicle.model.name.roadtrain,
            modelName = "RoadTrain",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    NEBULA(
            value = SAMPConstants.VEHICLE_NEBULA,
            textKey = TextKeys.vehicle.model.name.nebula,
            modelName = "Nebula",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    MAJESTIC(
            value = SAMPConstants.VEHICLE_MAJESTIC,
            textKey = TextKeys.vehicle.model.name.majestic,
            modelName = "Majestic",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    BUCCANEER(
            value = SAMPConstants.VEHICLE_BUCCANEER,
            textKey = TextKeys.vehicle.model.name.buccaneer,
            modelName = "Buccaneer",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    SHAMAL(
            value = SAMPConstants.VEHICLE_SHAMAL,
            textKey = TextKeys.vehicle.model.name.shamal,
            modelName = "Shamal",
            type = VehicleType.AIRCRAFT,
            numberOfSeats = 1
    ),
    HYDRA(
            value = SAMPConstants.VEHICLE_HYDRA,
            textKey = TextKeys.vehicle.model.name.hydra,
            modelName = "Hydra",
            type = VehicleType.AIRCRAFT,
            numberOfSeats = 1
    ),
    FCR900(
            value = SAMPConstants.VEHICLE_FCR900,
            textKey = TextKeys.vehicle.model.name.fcr900,
            modelName = "FCR-900",
            type = VehicleType.MOTORBIKE,
            numberOfSeats = 2
    ),
    NRG500(
            value = SAMPConstants.VEHICLE_NRG500,
            textKey = TextKeys.vehicle.model.name.nrg500,
            modelName = "NRG-500",
            type = VehicleType.MOTORBIKE,
            numberOfSeats = 2
    ),
    HPV1000(
            value = SAMPConstants.VEHICLE_HPV1000,
            textKey = TextKeys.vehicle.model.name.hpv1000,
            modelName = "HPV1000",
            type = VehicleType.MOTORBIKE,
            numberOfSeats = 2
    ),
    CEMENTTRUCK(
            value = SAMPConstants.VEHICLE_CEMENTTRUCK,
            textKey = TextKeys.vehicle.model.name.cementtruck,
            modelName = "Cement Truck",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    TOWTRUCK(
            value = SAMPConstants.VEHICLE_TOWTRUCK,
            textKey = TextKeys.vehicle.model.name.towtruck,
            modelName = "Tow Truck",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    FORTUNE(
            value = SAMPConstants.VEHICLE_FORTUNE,
            textKey = TextKeys.vehicle.model.name.fortune,
            modelName = "Fortune",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    CADRONA(
            value = SAMPConstants.VEHICLE_CADRONA,
            textKey = TextKeys.vehicle.model.name.cadrona,
            modelName = "Cadrona",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    FBITRUCK(
            value = SAMPConstants.VEHICLE_FBITRUCK,
            textKey = TextKeys.vehicle.model.name.fbitruck,
            modelName = "FBI Truck",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    WILLARD(
            value = SAMPConstants.VEHICLE_WILLARD,
            textKey = TextKeys.vehicle.model.name.willard,
            modelName = "Willard",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    FORKLIFT(
            value = SAMPConstants.VEHICLE_FORKLIFT,
            textKey = TextKeys.vehicle.model.name.forklift,
            modelName = "Forklift",
            type = VehicleType.CAR,
            numberOfSeats = 1
    ),
    TRACTOR(
            value = SAMPConstants.VEHICLE_TRACTOR,
            textKey = TextKeys.vehicle.model.name.tractor,
            modelName = "Tractor",
            type = VehicleType.CAR,
            numberOfSeats = 1
    ),
    COMBINE(
            value = SAMPConstants.VEHICLE_COMBINE,
            textKey = TextKeys.vehicle.model.name.combine,
            modelName = "Combine",
            type = VehicleType.CAR,
            numberOfSeats = 1
    ),
    FELTZER(
            value = SAMPConstants.VEHICLE_FELTZER,
            textKey = TextKeys.vehicle.model.name.feltzer,
            modelName = "Feltzer",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    REMINGTON(
            value = SAMPConstants.VEHICLE_REMINGTON,
            textKey = TextKeys.vehicle.model.name.remington,
            modelName = "Remington",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    SLAMVAN(
            value = SAMPConstants.VEHICLE_SLAMVAN,
            textKey = TextKeys.vehicle.model.name.slamvan,
            modelName = "Slamvan",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    BLADE(
            value = SAMPConstants.VEHICLE_BLADE,
            textKey = TextKeys.vehicle.model.name.blade,
            modelName = "Blade",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    FREIGHT(
            value = SAMPConstants.VEHICLE_FREIGHT,
            textKey = TextKeys.vehicle.model.name.freight,
            modelName = "Freight",
            type = VehicleType.TRAIN,
            numberOfSeats = 2
    ),
    BROWNSTREAK(
            value = SAMPConstants.VEHICLE_BROWNSTREAK,
            textKey = TextKeys.vehicle.model.name.brownstreak,
            modelName = "Streak",
            type = VehicleType.TRAIN,
            numberOfSeats = 2
    ),
    VORTEX(
            value = SAMPConstants.VEHICLE_VORTEX,
            textKey = TextKeys.vehicle.model.name.vortex,
            modelName = "Vortex",
            type = VehicleType.BOAT,
            numberOfSeats = 0
    ),
    VINCENT(
            value = SAMPConstants.VEHICLE_VINCENT,
            textKey = TextKeys.vehicle.model.name.vincent,
            modelName = "Vincent",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    BULLET(
            value = SAMPConstants.VEHICLE_BULLET,
            textKey = TextKeys.vehicle.model.name.bullet,
            modelName = "Bullet",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    CLOVER(
            value = SAMPConstants.VEHICLE_CLOVER,
            textKey = TextKeys.vehicle.model.name.clover,
            modelName = "Clover",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    SADLER(
            value = SAMPConstants.VEHICLE_SADLER,
            textKey = TextKeys.vehicle.model.name.sadler,
            modelName = "Sadler",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    FIRETRUCKLA(
            value = SAMPConstants.VEHICLE_FIRETRUCKLA,
            textKey = TextKeys.vehicle.model.name.firetruckla,
            modelName = "Firetruck",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    HUSTLER(
            value = SAMPConstants.VEHICLE_HUSTLER,
            textKey = TextKeys.vehicle.model.name.hustler,
            modelName = "Hustler",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    INTRUDER(
            value = SAMPConstants.VEHICLE_INTRUDER,
            textKey = TextKeys.vehicle.model.name.intruder,
            modelName = "Intruder",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    PRIMO(
            value = SAMPConstants.VEHICLE_PRIMO,
            textKey = TextKeys.vehicle.model.name.primo,
            modelName = "Primo",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    CARGOBOB(
            value = SAMPConstants.VEHICLE_CARGOBOB,
            textKey = TextKeys.vehicle.model.name.cargobob,
            modelName = "Cargobob",
            type = VehicleType.HELICOPTER,
            numberOfSeats = 2
    ),
    TAMPA(
            value = SAMPConstants.VEHICLE_TAMPA,
            textKey = TextKeys.vehicle.model.name.tampa,
            modelName = "Tampa",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    SUNRISE(
            value = SAMPConstants.VEHICLE_SUNRISE,
            textKey = TextKeys.vehicle.model.name.sunrise,
            modelName = "Sunrise",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    MERIT(
            value = SAMPConstants.VEHICLE_MERIT,
            textKey = TextKeys.vehicle.model.name.merit,
            modelName = "Merit",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    UTILITYVAN(
            value = SAMPConstants.VEHICLE_UTILITYVAN,
            textKey = TextKeys.vehicle.model.name.utilityvan,
            modelName = "Utility",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    NEVADA(
            value = SAMPConstants.VEHICLE_NEVADA,
            textKey = TextKeys.vehicle.model.name.nevada,
            modelName = "Nevada",
            type = VehicleType.AIRCRAFT,
            numberOfSeats = 1
    ),
    YOSEMITE(
            value = SAMPConstants.VEHICLE_YOSEMITE,
            textKey = TextKeys.vehicle.model.name.yosemite,
            modelName = "Yosemite",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    WINDSOR(
            value = SAMPConstants.VEHICLE_WINDSOR,
            textKey = TextKeys.vehicle.model.name.windsor,
            modelName = "Windsor",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    MONSTERA(
            value = SAMPConstants.VEHICLE_MONSTERA,
            textKey = TextKeys.vehicle.model.name.monstera,
            modelName = "Monster",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    MONSTERB(
            value = SAMPConstants.VEHICLE_MONSTERB,
            textKey = TextKeys.vehicle.model.name.monsterb,
            modelName = "Monster",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    URANUS(
            value = SAMPConstants.VEHICLE_URANUS,
            textKey = TextKeys.vehicle.model.name.uranus,
            modelName = "Uranus",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    JESTER(
            value = SAMPConstants.VEHICLE_JESTER,
            textKey = TextKeys.vehicle.model.name.jester,
            modelName = "Jester",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    SULTAN(
            value = SAMPConstants.VEHICLE_SULTAN,
            textKey = TextKeys.vehicle.model.name.sultan,
            modelName = "Sultan",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    STRATUM(
            value = SAMPConstants.VEHICLE_STRATUM,
            textKey = TextKeys.vehicle.model.name.stratum,
            modelName = "Stratum",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    ELEGY(
            value = SAMPConstants.VEHICLE_ELEGY,
            textKey = TextKeys.vehicle.model.name.elegy,
            modelName = "Elegy",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    RAINDANCE(
            value = SAMPConstants.VEHICLE_RAINDANCE,
            textKey = TextKeys.vehicle.model.name.raindance,
            modelName = "Raindance",
            type = VehicleType.HELICOPTER,
            numberOfSeats = 2
    ),
    RCTIGER(
            SAMPConstants.VEHICLE_RCTIGER,
            TextKeys.vehicle.model.name.rctiger,
            "RC Tiger",
            VehicleType.REMOTE_CONTROL,
            1
    ),
    FLASH(
            value = SAMPConstants.VEHICLE_FLASH,
            textKey = TextKeys.vehicle.model.name.flash,
            modelName = "Flash",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    TAHOMA(
            value = SAMPConstants.VEHICLE_TAHOMA,
            textKey = TextKeys.vehicle.model.name.tahoma,
            modelName = "Tahoma",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    SAVANNA(
            value = SAMPConstants.VEHICLE_SAVANNA,
            textKey = TextKeys.vehicle.model.name.savanna,
            modelName = "Savanna",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    BANDITO(
            value = SAMPConstants.VEHICLE_BANDITO,
            textKey = TextKeys.vehicle.model.name.bandito,
            modelName = "Bandito",
            type = VehicleType.CAR,
            numberOfSeats = 1
    ),
    FREIGHTFLATTRAILER(
            value = SAMPConstants.VEHICLE_FREIGHTFLATTRAILER,
            textKey = TextKeys.vehicle.model.name.freightflattrailer,
            modelName = "Freight",
            type = VehicleType.TRAIN,
            numberOfSeats = 0
    ),
    STREAKTRAILER(
            value = SAMPConstants.VEHICLE_STREAKTRAILER,
            textKey = TextKeys.vehicle.model.name.streaktrailer,
            modelName = "Trailer",
            type = VehicleType.TRAIN,
            numberOfSeats = 0
    ),
    KART(
            value = SAMPConstants.VEHICLE_KART,
            textKey = TextKeys.vehicle.model.name.kart,
            modelName = "Kart",
            type = VehicleType.CAR,
            numberOfSeats = 1
    ),
    MOWER(
            value = SAMPConstants.VEHICLE_MOWER,
            textKey = TextKeys.vehicle.model.name.mower,
            modelName = "Mower",
            type = VehicleType.CAR,
            numberOfSeats = 1
    ),
    DUNERIDE(
            value = SAMPConstants.VEHICLE_DUNERIDE,
            textKey = TextKeys.vehicle.model.name.duneride,
            modelName = "Duneride",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    SWEEPER(
            value = SAMPConstants.VEHICLE_SWEEPER,
            textKey = TextKeys.vehicle.model.name.sweeper,
            modelName = "Sweeper",
            type = VehicleType.CAR,
            numberOfSeats = 1
    ),
    BROADWAY(
            value = SAMPConstants.VEHICLE_BROADWAY,
            textKey = TextKeys.vehicle.model.name.broadway,
            modelName = "Broadway",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    TORNADO(
            value = SAMPConstants.VEHICLE_TORNADO,
            textKey = TextKeys.vehicle.model.name.tornado,
            modelName = "Tornado",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    AT400(
            value = SAMPConstants.VEHICLE_AT400,
            textKey = TextKeys.vehicle.model.name.at400,
            modelName = "AT-400",
            type = VehicleType.AIRCRAFT,
            numberOfSeats = 1
    ),
    DFT30(
            value = SAMPConstants.VEHICLE_DFT30,
            textKey = TextKeys.vehicle.model.name.dft30,
            modelName = "DFT-30",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    HUNTLEY(
            value = SAMPConstants.VEHICLE_HUNTLEY,
            textKey = TextKeys.vehicle.model.name.huntley,
            modelName = "Huntley",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    STAFFORD(
            value = SAMPConstants.VEHICLE_STAFFORD,
            textKey = TextKeys.vehicle.model.name.stafford,
            modelName = "Stafford",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    BF400(
            value = SAMPConstants.VEHICLE_BF400,
            textKey = TextKeys.vehicle.model.name.bf400,
            modelName = "BF-400",
            type = VehicleType.MOTORBIKE,
            numberOfSeats = 2
    ),
    NEWSVAN(
            value = SAMPConstants.VEHICLE_NEWSVAN,
            textKey = TextKeys.vehicle.model.name.newsvan,
            modelName = "Newsvan",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    TUG(
            value = SAMPConstants.VEHICLE_TUG,
            textKey = TextKeys.vehicle.model.name.tug,
            modelName = "Tug",
            type = VehicleType.CAR,
            numberOfSeats = 1
    ),
    PETROLTRAILER(
            value = SAMPConstants.VEHICLE_PETROLTRAILER,
            textKey = TextKeys.vehicle.model.name.petroltrailer,
            modelName = "Trailer",
            type = VehicleType.TRAILER,
            numberOfSeats = 0
    ),
    EMPEROR(
            value = SAMPConstants.VEHICLE_EMPEROR,
            textKey = TextKeys.vehicle.model.name.emperor,
            modelName = "Emperor",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    WAYFARER(
            value = SAMPConstants.VEHICLE_WAYFARER,
            textKey = TextKeys.vehicle.model.name.wayfarer,
            modelName = "Wayfarer",
            type = VehicleType.MOTORBIKE,
            numberOfSeats = 2
    ),
    EUROS(
            value = SAMPConstants.VEHICLE_EUROS,
            textKey = TextKeys.vehicle.model.name.euros,
            modelName = "Euros",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    HOTDOG(
            value = SAMPConstants.VEHICLE_HOTDOG,
            textKey = TextKeys.vehicle.model.name.hotdog,
            modelName = "Hotdog",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    CLUB(
            value = SAMPConstants.VEHICLE_CLUB,
            textKey = TextKeys.vehicle.model.name.club,
            modelName = "Club",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    FREIGHTBOXTRAILER(
            value = SAMPConstants.VEHICLE_FREIGHTBOXTRAILER,
            textKey = TextKeys.vehicle.model.name.freightboxtrailer,
            modelName = "Trailer",
            type = VehicleType.TRAILER,
            numberOfSeats = 0
    ),
    ARTICLETRAILER3(
            value = SAMPConstants.VEHICLE_ARTICLETRAILER3,
            textKey = TextKeys.vehicle.model.name.articletrailer3,
            modelName = "Trailer",
            type = VehicleType.TRAILER,
            numberOfSeats = 0
    ),
    ANDROMADA(
            value = SAMPConstants.VEHICLE_ANDROMADA,
            textKey = TextKeys.vehicle.model.name.andromada,
            modelName = "Andromada",
            type = VehicleType.AIRCRAFT,
            numberOfSeats = 2
    ),
    DODO(
            value = SAMPConstants.VEHICLE_DODO,
            textKey = TextKeys.vehicle.model.name.dodo,
            modelName = "Dodo",
            type = VehicleType.AIRCRAFT,
            numberOfSeats = 2
    ),
    RCCAM(
            value = SAMPConstants.VEHICLE_RCCAM,
            textKey = TextKeys.vehicle.model.name.rccam,
            modelName = "RC Cam",
            type = VehicleType.REMOTE_CONTROL,
            numberOfSeats = 1
    ),
    LAUNCH(
            value = SAMPConstants.VEHICLE_LAUNCH,
            textKey = TextKeys.vehicle.model.name.launch,
            modelName = "Launch",
            type = VehicleType.BOAT,
            numberOfSeats = 0
    ),
    POLICECARLSPD(
            value = SAMPConstants.VEHICLE_POLICECARLSPD,
            textKey = TextKeys.vehicle.model.name.policecarlspd,
            modelName = "Police Car LSPD",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    POLICECARSFPD(
            value = SAMPConstants.VEHICLE_POLICECARSFPD,
            textKey = TextKeys.vehicle.model.name.policecarsfpd,
            modelName = "Police Car SFPD",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    POLICECARLVPD(
            value = SAMPConstants.VEHICLE_POLICECARLVPD,
            textKey = TextKeys.vehicle.model.name.policecarlvpd,
            modelName = "Police Car LVPD",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    POLICERANGER(
            value = SAMPConstants.VEHICLE_POLICERANGER,
            textKey = TextKeys.vehicle.model.name.policeranger,
            modelName = "Police Ranger",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    PICADOR(
            value = SAMPConstants.VEHICLE_PICADOR,
            textKey = TextKeys.vehicle.model.name.picador,
            modelName = "Picador",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    SWAT(
            value = SAMPConstants.VEHICLE_SWAT,
            textKey = TextKeys.vehicle.model.name.swat,
            modelName = "SWAT Van",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    ALPHA(
            value = SAMPConstants.VEHICLE_ALPHA,
            textKey = TextKeys.vehicle.model.name.alpha,
            modelName = "Alpha",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    PHOENIX(
            value = SAMPConstants.VEHICLE_PHOENIX,
            textKey = TextKeys.vehicle.model.name.phoenix,
            modelName = "Phoenix",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    GLENDALESHIT(
            value = SAMPConstants.VEHICLE_GLENDALESHIT,
            textKey = TextKeys.vehicle.model.name.glendaleshit,
            modelName = "Glendale",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    SADLERSHIT(
            value = SAMPConstants.VEHICLE_SADLERSHIT,
            textKey = TextKeys.vehicle.model.name.sadlershit,
            modelName = "Sadler",
            type = VehicleType.CAR,
            numberOfSeats = 2
    ),
    BAGGAGETRAILERA(
            value = SAMPConstants.VEHICLE_BAGGAGETRAILERA,
            textKey = TextKeys.vehicle.model.name.baggagetrailera,
            modelName = "Luggage Trailer",
            type = VehicleType.TRAILER,
            numberOfSeats = 0
    ),
    BAGGAGETRAILERB(
            value = SAMPConstants.VEHICLE_BAGGAGETRAILERB,
            textKey = TextKeys.vehicle.model.name.baggagetrailerb,
            modelName = "Luggage Trailer",
            type = VehicleType.TRAILER,
            numberOfSeats = 0
    ),
    TUGSTAIRSTRAILER(
            value = SAMPConstants.VEHICLE_TUGSTAIRSTRAILER,
            textKey = TextKeys.vehicle.model.name.tugstairstrailer,
            modelName = "Stair Trailer",
            type = VehicleType.TRAILER,
            numberOfSeats = 0
    ),
    BOXBURG(
            value = SAMPConstants.VEHICLE_BOXBURG,
            textKey = TextKeys.vehicle.model.name.boxburg,
            modelName = "Boxville",
            type = VehicleType.CAR,
            numberOfSeats = 4
    ),
    FARMTRAILER(
            value = SAMPConstants.VEHICLE_FARMTRAILER,
            textKey = TextKeys.vehicle.model.name.farmtrailer,
            modelName = "Farm Plow",
            type = VehicleType.TRAILER,
            numberOfSeats = 0
    ),
    UTILITYTRAILER(
            value = SAMPConstants.VEHICLE_UTILITYTRAILER,
            textKey = TextKeys.vehicle.model.name.utilitytrailer,
            modelName = "Utility Trailer",
            type = VehicleType.TRAILER,
            numberOfSeats = 0
    );

    companion object : ConstantValueRegistry<Int, VehicleModel>(*VehicleModel.values()) {

        private val vehicleModelsByName = PatriciaTrie<VehicleModel>()

        init {
            // Only index models with unique names
            VehicleModel
                    .values()
                    .groupBy { it.modelName.toLowerCase() }
                    .values
                    .filter { it.size == 1 }
                    .map { it.first() }
                    .forEach {
                        vehicleModelsByName[it.modelName.toLowerCase()] = it
                    }
        }

        operator fun get(modelName: String): VehicleModel? {
            val models = vehicleModelsByName.prefixMap(modelName.toLowerCase()).values
            return when {
                models.size == 1 -> models.first()
                else -> null
            }
        }
    }

}
