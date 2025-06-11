package projetoSalf.mvc.model;

public class CategoriaProduto {
    private int id;
    private String desc;

    public CategoriaProduto() {}

    public CategoriaProduto(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "CategoriaProduto{" +
                "id=" + id +
                ", desc='" + desc + '\'' +
                '}';
    }
}
