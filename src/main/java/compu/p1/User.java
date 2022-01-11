package compu.p1;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class User implements Serializable {
    @Id @GeneratedValue Long id;
    @Column(name = "name") String name;
    @Column(name = "pass") String pass;

    @OneToMany(mappedBy = "user")
    Set<FileNode> files;


    public void addFile(FileNode fn){
        files.add(fn);
        fn.setUser(this);
    }
    public void removeFile(FileNode fn){
        files.add(fn);
        fn.setUser(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    User() {
    }
    User(String name, String pass) {
        this.name = name;
        this.pass = pass;
        this.files = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(pass, user.pass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pass);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
