package ch.leadrian.samp.kamp.common.neon

import ch.leadrian.samp.kamp.core.api.constants.VehicleModel
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.ADMIRAL
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.ALPHA
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.AMBULANCE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BAGGAGE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BANDITO
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BANSHEE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BARRACKS
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BENSON
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BERKLEYSRCVAN
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BFINJECTION
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BLADE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BLISTACOMPACT
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BLOODRINGBANGER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BOBCAT
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BOXBURG
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BOXVILLE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BRAVURA
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BROADWAY
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BUCCANEER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BUFFALO
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BULLET
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BURRITO
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.BUS
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.CABBIE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.CADRONA
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.CAMPER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.CARGOBOB
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.CEMENTTRUCK
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.CHEETAH
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.CLOVER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.CLUB
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.COACH
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.COMET
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.DFT30
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.DUMPER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.ELEGANT
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.ELEGY
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.EMPEROR
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.ENFORCER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.ESPERANTO
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.EUROS
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.FBIRANCHER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.FBITRUCK
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.FELTZER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.FIRETRUCK
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.FIRETRUCKLA
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.FLASH
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.FLATBED
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.FORTUNE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.GLENDALE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.GLENDALESHIT
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.GREENWOOD
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.HERMES
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.HOTDOG
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.HOTRINGRACER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.HOTRINGRACERA
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.HOTRINGRACERB
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.HUNTER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.HUNTLEY
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.HUSTLER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.INFERNUS
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.INTRUDER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.JESTER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.JOURNEY
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.LANDSTALKER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.LEVIATHAN
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.LINERUNNER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.MAJESTIC
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.MANANA
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.MERIT
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.MESA
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.MONSTER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.MONSTERA
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.MONSTERB
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.MOONBEAM
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.MRWHOOPEE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.MULE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.NEBULA
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.NEWSVAN
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.OCEANIC
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.PACKER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.PATRIOT
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.PERRENIAL
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.PHOENIX
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.PICADOR
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.POLICECARLSPD
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.POLICECARLVPD
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.POLICECARSFPD
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.POLICERANGER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.PONY
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.PREMIER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.PREVION
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.PRIMO
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.RANCHER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.RANCHERLURE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.REGINA
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.REMINGTON
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.ROADTRAIN
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.ROMERO
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.RUMPO
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.SABRE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.SADLER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.SADLERSHIT
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.SANDKING
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.SAVANNA
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.SEASPARROW
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.SECURICAR
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.SENTINEL
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.SKIMMER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.SLAMVAN
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.SOLAIR
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.STAFFORD
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.STALLION
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.STRATUM
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.STRETCH
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.SULTAN
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.SUNRISE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.SUPERGT
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.TAHOMA
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.TAMPA
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.TANKER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.TAXI
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.TORNADO
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.TOWTRUCK
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.TRACTOR
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.TRASHMASTER
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.TUG
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.TURISMO
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.URANUS
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.UTILITYVAN
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.VINCENT
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.VIRGO
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.VOODOO
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.VORTEX
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.WALTON
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.WASHINGTON
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.WILLARD
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.WINDSOR
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.YANKEE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.YOSEMITE
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel.ZR350
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import java.util.EnumMap

object NeonOffset {

    private val offsets: MutableMap<VehicleModel, Vector3D> = EnumMap<VehicleModel, Vector3D>(VehicleModel::class.java)

    init {
        offsets[LANDSTALKER] = vector3DOf(-0.924999f, 0.009999f, -0.674999f)
        offsets[BRAVURA] = vector3DOf(-0.979999f, 0.074999f, -0.489999f)
        offsets[BUFFALO] = vector3DOf(-1.034999f, -0.029999f, -0.579999f)
        offsets[LINERUNNER] = vector3DOf(-0.884999f, 1.834998f, -1.099999f)
        offsets[PERRENIAL] = vector3DOf(-0.839999f, -0.064999f, -0.419999f)
        offsets[SENTINEL] = vector3DOf(-0.914999f, 0.0f, -0.534999f)
        offsets[DUMPER] = vector3DOf(-1.129999f, 0.354999f, -1.219999f)
        offsets[FIRETRUCK] = vector3DOf(-1.214999f, 0.125f, -0.759999f)
        offsets[TRASHMASTER] = vector3DOf(-0.664999f, 0.709999f, -0.889999f)
        offsets[STRETCH] = vector3DOf(-0.914999f, -0.294999f, -0.474999f)
        offsets[MANANA] = vector3DOf(-0.879999f, 0.0f, -0.389999f)
        offsets[INFERNUS] = vector3DOf(-0.969999f, 0.004999f, -0.559999f)
        offsets[VOODOO] = vector3DOf(-0.994999f, -0.154999f, -0.569999f)
        offsets[PONY] = vector3DOf(-0.919999f, -0.069999f, -0.719999f)
        offsets[MULE] = vector3DOf(-0.759999f, -0.469999f, -0.524999f)
        offsets[CHEETAH] = vector3DOf(-0.949999f, -0.009999f, -0.524999f)
        offsets[AMBULANCE] = vector3DOf(-1.259999f, -0.604999f, -0.664999f)
        offsets[LEVIATHAN] = vector3DOf(-2.255004f, 0.859999f, -0.124999f)
        offsets[MOONBEAM] = vector3DOf(-1.004999f, 0.089999f, -0.829999f)
        offsets[ESPERANTO] = vector3DOf(-0.934999f, -0.009999f, -0.584999f)
        offsets[TAXI] = vector3DOf(-0.954999f, -0.009999f, -0.444999f)
        offsets[WASHINGTON] = vector3DOf(-0.929999f, -0.029999f, -0.589999f)
        offsets[BOBCAT] = vector3DOf(-0.889999f, 0.009999f, -0.609999f)
        offsets[MRWHOOPEE] = vector3DOf(-0.999999f, 0.164999f, -0.714999f)
        offsets[BFINJECTION] = vector3DOf(-0.739999f, 0.039999f, -0.234999f)
        offsets[HUNTER] = vector3DOf(-0.639999f, 2.475009f, -0.789999f)
        offsets[PREMIER] = vector3DOf(-0.969999f, 0.0f, -0.464999f)
        offsets[ENFORCER] = vector3DOf(-1.124999f, -0.714999f, -0.734999f)
        offsets[SECURICAR] = vector3DOf(-1.124999f, 0.054999f, -0.759999f)
        offsets[BANSHEE] = vector3DOf(-0.844999f, 0.004999f, -0.474999f)
        offsets[BUS] = vector3DOf(-1.304998f, 2.300005f, -0.714999f)
        offsets[BARRACKS] = vector3DOf(-0.579999f, 0.994999f, -0.874999f)
        offsets[PREVION] = vector3DOf(-0.859999f, -0.024999f, -0.489999f)
        offsets[COACH] = vector3DOf(-1.314998f, 2.245003f, -0.724999f)
        offsets[CABBIE] = vector3DOf(-0.969999f, -0.034999f, -0.674999f)
        offsets[STALLION] = vector3DOf(-0.884999f, 0.034999f, -0.644999f)
        offsets[RUMPO] = vector3DOf(-0.914999f, -0.045f, -0.899999f)
        offsets[ROMERO] = vector3DOf(-1.004999f, -0.074999f, -0.604999f)
        offsets[PACKER] = vector3DOf(-0.809999f, 2.770015f, -1.174999f)
        offsets[MONSTER] = vector3DOf(-1.064999f, 0.0f, 0.135f)
        offsets[ADMIRAL] = vector3DOf(-0.949999f, 0.0f, -0.519999f)
        offsets[SEASPARROW] = vector3DOf(-1.004999f, 0.259999f, -0.769999f)
        offsets[TURISMO] = vector3DOf(-0.984999f, -0.194999f, -0.519999f)
        offsets[FLATBED] = vector3DOf(-0.579999f, 0.979999f, -0.859999f)
        offsets[YANKEE] = vector3DOf(-0.764999f, -0.179999f, -0.464999f)
        offsets[SOLAIR] = vector3DOf(-0.959999f, -0.039999f, -0.594999f)
        offsets[BERKLEYSRCVAN] = vector3DOf(-0.919999f, -0.069999f, -0.719999f)
        offsets[SKIMMER] = vector3DOf(-1.399998f, 0.554999f, -1.819998f)
        offsets[GLENDALE] = vector3DOf(-0.989999f, -0.064999f, -0.489999f)
        offsets[OCEANIC] = vector3DOf(-0.989999f, -0.044999f, -0.484999f)
        offsets[PATRIOT] = vector3DOf(-1.119999f, -0.149999f, -0.359999f)
        offsets[HERMES] = vector3DOf(-0.994999f, -0.064999f, -0.544999f)
        offsets[SABRE] = vector3DOf(-0.904999f, -0.054999f, -0.549999f)
        offsets[ZR350] = vector3DOf(-0.979999f, 0.019999f, -0.514999f)
        offsets[WALTON] = vector3DOf(-0.804999f, 0.064999f, -0.519999f)
        offsets[REGINA] = vector3DOf(-0.854999f, 0.09f, -0.454999f)
        offsets[COMET] = vector3DOf(-0.879999f, 0.0f, -0.489999f)
        offsets[BURRITO] = vector3DOf(-0.989999f, 0.0f, -0.759999f)
        offsets[CAMPER] = vector3DOf(-0.834999f, -0.149999f, -0.744999f)
        offsets[BAGGAGE] = vector3DOf(-0.629999f, 0.164999f, -0.374999f)
        offsets[RANCHER] = vector3DOf(-1.019999f, 0.11f, -0.679999f)
        offsets[FBIRANCHER] = vector3DOf(-1.054999f, 0.125f, -0.604999f)
        offsets[VIRGO] = vector3DOf(-0.874999f, -0.009999f, -0.569999f)
        offsets[GREENWOOD] = vector3DOf(-0.909999f, 0.0f, -0.459999f)
        offsets[HOTRINGRACER] = vector3DOf(-0.909999f, -0.199999f, -0.639999f)
        offsets[SANDKING] = vector3DOf(-1.024999f, 0.184999f, -0.809999f)
        offsets[BLISTACOMPACT] = vector3DOf(-0.854999f, 0.029999f, -0.469999f)
        offsets[BOXVILLE] = vector3DOf(-1.149999f, 0.269999f, -0.654999f)
        offsets[BENSON] = vector3DOf(-0.969999f, -1.404998f, -0.199999f)
        offsets[MESA] = vector3DOf(-0.969999f, 0.189999f, -0.574999f)
        offsets[HOTRINGRACERA] = vector3DOf(-0.909999f, 0.029999f, -0.639999f)
        offsets[HOTRINGRACERB] = vector3DOf(-0.909999f, -0.184999f, -0.639999f)
        offsets[BLOODRINGBANGER] = vector3DOf(-0.989999f, -0.059999f, -0.494999f)
        offsets[RANCHERLURE] = vector3DOf(-1.019999f, 0.11f, -0.679999f)
        offsets[SUPERGT] = vector3DOf(-0.904999f, -0.249999f, -0.509999f)
        offsets[ELEGANT] = vector3DOf(-1.084999f, -0.024999f, -0.589999f)
        offsets[JOURNEY] = vector3DOf(-1.334998f, -0.424999f, -0.979999f)
        offsets[TANKER] = vector3DOf(-0.969999f, 1.669998f, -1.174999f)
        offsets[ROADTRAIN] = vector3DOf(-1.454998f, 0.439999f, -1.854998f)
        offsets[NEBULA] = vector3DOf(-0.989999f, 0.024999f, -0.584999f)
        offsets[MAJESTIC] = vector3DOf(-0.959999f, 0.004999f, -0.554999f)
        offsets[BUCCANEER] = vector3DOf(-1.049999f, 0.11f, -0.439999f)
        offsets[CEMENTTRUCK] = vector3DOf(-0.689999f, 1.489998f, -1.319998f)
        offsets[TOWTRUCK] = vector3DOf(-1.054999f, 0.499999f, -0.329999f)
        offsets[FORTUNE] = vector3DOf(-0.899999f, 0.0f, -0.564999f)
        offsets[CADRONA] = vector3DOf(-0.944999f, 0.154999f, -0.419999f)
        offsets[FBITRUCK] = vector3DOf(-0.924999f, 0.059999f, -0.529999f)
        offsets[WILLARD] = vector3DOf(-0.934999f, -0.11f, -0.429999f)
        offsets[TRACTOR] = vector3DOf(-0.144999f, 0.314999f, -0.444999f)
        offsets[FELTZER] = vector3DOf(-0.924999f, -0.034999f, -0.479999f)
        offsets[REMINGTON] = vector3DOf(-0.984999f, 0.164999f, -0.564999f)
        offsets[SLAMVAN] = vector3DOf(-1.069999f, -0.034999f, -0.569999f)
        offsets[BLADE] = vector3DOf(-0.954999f, -0.019999f, -0.579999f)
        offsets[VORTEX] = vector3DOf(-1.004999f, -0.049999f, -0.019999f)
        offsets[VINCENT] = vector3DOf(-1.024999f, -0.09f, -0.639999f)
        offsets[BULLET] = vector3DOf(-0.904999f, 0.0f, -0.414999f)
        offsets[CLOVER] = vector3DOf(-0.894999f, 0.014999f, -0.539999f)
        offsets[SADLER] = vector3DOf(-0.904999f, 0.009999f, -0.409999f)
        offsets[FIRETRUCKLA] = vector3DOf(-1.089999f, 0.0f, -0.709999f)
        offsets[HUSTLER] = vector3DOf(-0.939999f, -0.029999f, -0.564999f)
        offsets[INTRUDER] = vector3DOf(-1.039999f, -0.1f, -0.474999f)
        offsets[PRIMO] = vector3DOf(-0.884999f, 0.059999f, -0.479999f)
        offsets[CARGOBOB] = vector3DOf(-1.769998f, 1.484998f, -1.774998f)
        offsets[TAMPA] = vector3DOf(-0.949999f, 0.105f, -0.439999f)
        offsets[SUNRISE] = vector3DOf(-1.089999f, -0.059999f, -0.554999f)
        offsets[MERIT] = vector3DOf(-0.964999f, 0.004999f, -0.564999f)
        offsets[UTILITYVAN] = vector3DOf(-1.034999f, 0.749999f, -0.254999f)
        offsets[YOSEMITE] = vector3DOf(-1.109999f, 0.009999f, -0.534999f)
        offsets[WINDSOR] = vector3DOf(-0.754999f, 0.0f, -0.464999f)
        offsets[MONSTERA] = vector3DOf(-1.119999f, 0.0f, 0.184999f)
        offsets[MONSTERB] = vector3DOf(-1.129999f, 0.044999f, 0.074999f)
        offsets[URANUS] = vector3DOf(-0.894999f, -0.089999f, -0.374999f)
        offsets[JESTER] = vector3DOf(-0.964999f, 0.0f, -0.444999f)
        offsets[SULTAN] = vector3DOf(-1.004999f, 0.054999f, -0.414999f)
        offsets[STRATUM] = vector3DOf(-0.919999f, 0.13f, -0.574999f)
        offsets[ELEGY] = vector3DOf(-0.909999f, 0.12f, -0.404999f)
        offsets[FLASH] = vector3DOf(-0.824999f, 0.0f, -0.414999f)
        offsets[TAHOMA] = vector3DOf(-0.989999f, -0.049999f, -0.479999f)
        offsets[SAVANNA] = vector3DOf(-1.089999f, 0.309999f, -0.609999f)
        offsets[BANDITO] = vector3DOf(-0.334999f, 0.1f, -0.374999f)
        offsets[BROADWAY] = vector3DOf(-0.924999f, 0.004999f, -0.334999f)
        offsets[TORNADO] = vector3DOf(-1.019999f, -0.14f, -0.419999f)
        offsets[DFT30] = vector3DOf(-1.429998f, -0.649999f, -0.879999f)
        offsets[HUNTLEY] = vector3DOf(-1.014999f, 0.0f, -0.484999f)
        offsets[STAFFORD] = vector3DOf(-1.089999f, 0.014999f, -0.474999f)
        offsets[NEWSVAN] = vector3DOf(-0.984999f, -0.194999f, -0.709999f)
        offsets[TUG] = vector3DOf(-0.674999f, 0.259999f, 0.13f)
        offsets[EMPEROR] = vector3DOf(-1.019999f, -0.009999f, -0.354999f)
        offsets[EUROS] = vector3DOf(-1.009999f, -0.064999f, -0.534999f)
        offsets[HOTDOG] = vector3DOf(-1.374998f, 0.0f, -0.664999f)
        offsets[CLUB] = vector3DOf(-0.909999f, 0.0f, -0.359999f)
        offsets[POLICECARLSPD] = vector3DOf(-0.974999f, 0.0f, -0.469999f)
        offsets[POLICECARSFPD] = vector3DOf(-0.969999f, 0.0f, -0.464999f)
        offsets[POLICECARLVPD] = vector3DOf(-1.004999f, -0.014999f, -0.414999f)
        offsets[POLICERANGER] = vector3DOf(-1.019999f, 0.115f, -0.684999f)
        offsets[PICADOR] = vector3DOf(-0.959999f, 0.204999f, -0.499999f)
        offsets[ALPHA] = vector3DOf(-1.024999f, 0.0f, -0.549999f)
        offsets[PHOENIX] = vector3DOf(-0.954999f, 0.034999f, -0.629999f)
        offsets[GLENDALESHIT] = vector3DOf(-0.989999f, -0.064999f, -0.489999f)
        offsets[SADLERSHIT] = vector3DOf(-0.904999f, 0.009999f, -0.409999f)
        offsets[BOXBURG] = vector3DOf(-1.149999f, 0.269999f, -0.654999f)
    }

    operator fun get(vehicleModel: VehicleModel): Vector3D? = offsets[vehicleModel]

}