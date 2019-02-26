/*
 * This project is licensed under GPLv3
 * https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.civrealms.civrealmstutorial;

/**
 *
 * @author crimeo
 */


public class PlayerProfile {
    byte[] triggerHistory;
    byte tutorialMode;
    
    public void PlayerProfile(){
        tutorialMode = 1;
        triggerHistory = new byte[100];
    }
    
}
