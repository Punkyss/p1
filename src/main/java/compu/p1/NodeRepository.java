package compu.p1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface NodeRepository extends JpaRepository<FileNode, Long> {

    List<FileNode> findBydescriptionContaining(String description);
    List<FileNode> findBykeywords(String[] keywords);
    List<FileNode> findAllByUser(User user);

    //@Query("SELECT t FROM FileNode t WHERE t.Description = ?1 ")
    //List<FileNode> findByDescription(String Description);
}
