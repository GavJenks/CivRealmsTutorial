/*
 * This project is licensed under GPLv3
 * https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.civrealms.civrealmstutorial;

import static com.civrealms.civrealmstutorial.CivRealmsTutorialListener.LOG;
import java.util.logging.Logger;

/**
 *
 * @author crimeo
 */

public class PlayerProfile {
    public byte[] triggerHistory = new byte[100];
    public byte tutorialMode = 1;
    public static Logger LOG = Logger.getLogger("CivRealmsPVE");
    
    public void PlayerProfile(){
        
    }
}
