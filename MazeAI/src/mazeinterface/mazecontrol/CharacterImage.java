package mazeinterface.mazecontrol;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

    /**
     * Lớp này dùng để hiển thị ảnh nhân vật <p>
     * Có phương thức để làm sáng và làm mờ ảnh <p>
     */
public class CharacterImage extends JLabel
{
    public String character = "";
    private ImageIcon avatar;
    private ImageIcon dimmedAvatar;
    public CharacterImage() {
        super(); // Hiển thị ảnh nhân vật
    }

    /** 
     * Làm sáng ảnh nhân vật <p>
     */
    public void highlight() {
        setIcon(avatar); // Hiển thị ảnh nhân vật
    }

    /**
     * Làm mờ ảnh nhân vật <p>
     */
    public void dimmed() {
        setIcon(dimmedAvatar); // Hiển thị ảnh nhân vật mờ
    }

    /**
     * Tải dữ liệu mới cho nhân vật <p>
     * @param character Tên người nói
     * @param avatar Ảnh nhân vật
     */
    public void load(String character, ImageIcon avatar) {
        this.character = character; // Gán tên người nói
        this.avatar = avatar; // Gán ảnh nhân vật
        this.dimmedAvatar = new ImageIcon(GrayFilter.createDisabledImage(avatar.getImage())); // Tạo ảnh mờ
    }
} 

