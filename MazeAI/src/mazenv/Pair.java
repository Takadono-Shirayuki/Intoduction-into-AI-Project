package mazenv;

import java.io.Serializable;

public class Pair<T1, T2> implements Serializable{
    private T1 item1;
    private T2 item2;

    /**
     * Lớp Pair dùng để lưu trữ một cặp giá trị. <p>
     * @param item1 Giá trị thứ nhất.
     * @param item2 Giá trị thứ hai.
     */
    public Pair(T1 item1, T2 item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    /**
     * Lấy giá trị thứ nhất trong cặp.
     * @return Giá trị thứ nhất.
     */
    public T1 getItem1() {
        return item1;
    }

    /**
     * Lấy giá trị thứ hai trong cặp.
     * @return Giá trị thứ hai.
     */
    public T2 getItem2() {
        return item2;
    }
}
