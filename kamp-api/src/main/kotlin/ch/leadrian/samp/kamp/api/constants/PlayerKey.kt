package ch.leadrian.samp.kamp.api.constants

enum class PlayerKey(
        val value: Int,
        onFootTextDrawCode: String? = null,
        inVehicleTextDrawCode: String? = null
) {

    ACTION(
                  value = SAMPConstants.KEY_ACTION,
                  onFootTextDrawCode = "PED_ANSWER_PHONE",
                  inVehicleTextDrawCode = "VEHICLE_FIREWEAPON_ALT"
          ),
    CROUCH(
                  value = SAMPConstants.KEY_CROUCH,
                  onFootTextDrawCode = "PED_DUCK",
                  inVehicleTextDrawCode = "VEHICLE_HORN"
          ),
    FIRE(
                value = SAMPConstants.KEY_FIRE,
                onFootTextDrawCode = "PED_FIREWEAPON",
                inVehicleTextDrawCode = "VEHICLE_FIREWEAPON"
        ),
    SPRINT(
                  value = SAMPConstants.KEY_SPRINT,
                  onFootTextDrawCode = "PED_SPRINT",
                  inVehicleTextDrawCode = "VEHICLE_ACCELERATE"
          ),
    SECONDARY_ATTACK(
                            value = SAMPConstants.KEY_SECONDARY_ATTACK,
                            onFootTextDrawCode = "VEHICLE_ENTER_EXIT",
                            inVehicleTextDrawCode = "VEHICLE_ENTER_EXIT"
                    ),
    JUMP(
                value = SAMPConstants.KEY_JUMP,
                onFootTextDrawCode = "PED_JUMPING",
                inVehicleTextDrawCode = "VEHICLE_BRAKE"
        ),
    LOOK_RIGHT(
                      value = SAMPConstants.KEY_LOOK_RIGHT,
                      inVehicleTextDrawCode = "VEHICLE_LOOKRIGHT"
              ),
    // Same as HANDBRAKE, not defined in SA-MP includes
    KEY_AIM(
                   value = SAMPConstants.KEY_HANDBRAKE,
                   onFootTextDrawCode = "PED_LOCK_TARGET",
                   inVehicleTextDrawCode = "VEHICLE_HANDBRAKE"
           ),
    HANDBRAKE(
                     value = SAMPConstants.KEY_HANDBRAKE,
                     onFootTextDrawCode = "PED_LOCK_TARGET",
                     inVehicleTextDrawCode = "VEHICLE_HANDBRAKE"
             ),
    LOOK_LEFT(
                     value = SAMPConstants.KEY_LOOK_LEFT,
                     inVehicleTextDrawCode = "VEHICLE_LOOKLEFT"
             ),
    SUBMISSION(
                      value = SAMPConstants.KEY_SUBMISSION,
                      inVehicleTextDrawCode = "TOGGLE_SUBMISSIONS"
              ),
    LOOK_BEHIND(
                       value = SAMPConstants.KEY_LOOK_BEHIND,
                       onFootTextDrawCode = "PED_LOOKBEHIND",
                       inVehicleTextDrawCode = "VEHICLE_LOOKBEHIND"
               ),
    WALK(
                value = SAMPConstants.KEY_WALK,
                onFootTextDrawCode = "SNEAK_ABOUT"
        ),
    ANALOG_UP(
                     value = SAMPConstants.KEY_ANALOG_UP,
                     inVehicleTextDrawCode = "VEHICLE_TURRETUP"
             ),
    ANALOG_DOWN(
                       value = SAMPConstants.KEY_ANALOG_DOWN,
                       inVehicleTextDrawCode = "VEHICLE_TURRETDOWN"
               ),
    ANALOG_LEFT(
                       value = SAMPConstants.KEY_ANALOG_LEFT,
                       onFootTextDrawCode = "VEHICLE_LOOKLEFT",
                       inVehicleTextDrawCode = "VEHICLE_TURRETLEFT"
               ),
    ANALOG_RIGHT(
                        value = SAMPConstants.KEY_ANALOG_RIGHT,
                        onFootTextDrawCode = "VEHICLE_LOOKRIGHT",
                        inVehicleTextDrawCode = "VEHICLE_TURRETRIGHT"
                ),
    YES(
               value = SAMPConstants.KEY_YES,
               onFootTextDrawCode = "CONVERSATION_YES",
               inVehicleTextDrawCode = "CONVERSATION_YES"
       ),
    NO(
              value = SAMPConstants.KEY_NO,
              onFootTextDrawCode = "CONVERSATION_NO",
              inVehicleTextDrawCode = "CONVERSATION_NO"
      ),
    CTRL_BACK(
                     value = SAMPConstants.KEY_CTRL_BACK,
                     onFootTextDrawCode = "GROUP_CONTROL_BWD",
                     inVehicleTextDrawCode = "GROUP_CONTROL_BWD"
             ),
    UP(
              value = SAMPConstants.KEY_UP,
              onFootTextDrawCode = "GO_FORWARD",
              inVehicleTextDrawCode = "VEHICLE_STEERUP"
      ),
    DOWN(
                value = SAMPConstants.KEY_DOWN,
                onFootTextDrawCode = "GO_BACK",
                inVehicleTextDrawCode = "VEHICLE_STEERDOWN"
        ),
    LEFT(
                value = SAMPConstants.KEY_LEFT,
                onFootTextDrawCode = "GO_LEFT",
                inVehicleTextDrawCode = "VEHICLE_STEERLEFT"
        ),
    RIGHT(
                 value = SAMPConstants.KEY_RIGHT,
                 onFootTextDrawCode = "GO_RIGHT",
                 inVehicleTextDrawCode = "VEHICLE_STEERRIGHT"
         );

    val onFootTextDrawCode: String = onFootTextDrawCode?.let { "~k~~$it~" } ?: "???"

    val inVehicleTextDrawCode: String = inVehicleTextDrawCode?.let { "~k~~$it~" } ?: "???"

}
