package compu.p1;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import javax.persistence.*;


@Entity
class FileNode  implements Serializable {
    @Id @GeneratedValue long Hash;
    @Column(name = "title") String title;
    @Column(name = "keywords") String[] keywords;
    @Column(name = "description") String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /*
        Criteria criteria = session.createCriteria(YourClass.class);
        YourObject yourObject = criteria.add(Restrictions.eq("yourField", yourFieldValue)).uniqueResult();
        List<YourObject> list = criteria.add(Restrictions.eq("yourField", yourFieldValue)).list();
    */
    FileNode(){}

    public FileNode( String title, String[] keywords, String description) {
        this.title = title;
        this.keywords = keywords;
        this.description = description;
    }

    public long getHash() {
        return Hash;
    }

    public void setHash(long hash) {
        Hash = hash;
    }


    public String getTitle() {
        return title;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileNode fileNode = (FileNode) o;
        return Hash == fileNode.Hash && Objects.equals(title, fileNode.title) && Arrays.equals(keywords, fileNode.keywords) && Objects.equals(description, fileNode.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(Hash, title, description);
        result = 31 * result + Arrays.hashCode(keywords);
        return result;
    }

    @Override
    public String toString() {
        return "FileNode{" +
                "Hash=" + Hash +
                ", title='" + title + '\'' +
                ", keywords=" + Arrays.
                toString(keywords) +
                ", description='" + description + '\'' +
                '}';
    }
}
