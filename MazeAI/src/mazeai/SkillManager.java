package mazeai;
import mazenv.*;

public class SkillManager {
    private MazeEnv mazeEnv;

    public SkillManager(MazeEnv mazeEnv) {
        this.mazeEnv = mazeEnv;
    }

    public void activateSenrigan() {
        mazeEnv.regenerateMaze(MazeEnv.Buff.SENRIGAN, MazeEnv.Debuff.NONE);
        System.out.println("Kỹ năng Senrigan kích hoạt! Tăng tầm nhìn");
    }
    
    public void activateTouNoHikari() {
        mazeEnv.regenerateMaze(MazeEnv.Buff.TOU_NO_HIKARI, MazeEnv.Debuff.NONE);
        System.out.println("Kỹ năng Tou no Hikari kích hoạt! Quan sát rộng");
    }
    
    public void activateUnmeiNoMichi() {
        mazeEnv.regenerateMaze(MazeEnv.Buff.UNMEI_NO_MICHI, MazeEnv.Debuff.NONE);
        System.out.println("Kỹ năng Unmei no Michi kích hoạt! Tạo đường");
    }
    
    public void activateSlimeSanOnegai() {
        mazeEnv.regenerateMaze(MazeEnv.Buff.SLIME_SAN_ONEGAI, MazeEnv.Debuff.NONE);
        System.out.println("Kỹ năng Slime San Onegai kích hoạt! Dò đường");
    }
    

    public void useSkill(String skillName, int x, int y) {
        switch (skillName) {
            case "Senrigan":
                activateSenrigan();
                break;
            case "Tou no Hikari":
                activateTouNoHikari();
                break;
            case "Unmei no Michi":
                activateUnmeiNoMichi();
                break;
            case "Slime San Onegai":
                activateSlimeSanOnegai();
                break;
            default:
                System.out.println("Kỹ năng không hợp lệ: " + skillName);
        }
    }
}