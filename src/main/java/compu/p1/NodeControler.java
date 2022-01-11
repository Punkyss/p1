package compu.p1;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sun.istack.NotNull;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.data.domain.Example;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class NodeControler{

    private final NodeRepository repository;
    private final UserRepository userrepository;
    private final NodeModelAssembler assembler;

    NodeControler(NodeRepository repository, NodeModelAssembler assembler, UserRepository userrepository) {
        this.repository = repository;
        this.assembler = assembler;
        this.userrepository = userrepository;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/files")
    CollectionModel<EntityModel<FileNode>> all() {

        List<EntityModel<FileNode>> files = repository.findAll().stream()
                .map(assembler::toModel) //
                .collect(Collectors.toList());
                /*.map(file -> EntityModel.of(file,
                        linkTo(methodOn(NodeControler.class).one(file.getHash())).withSelfRel(),
                        linkTo(methodOn(NodeControler.class).all()).withRel("employees")))
                .collect(Collectors.toList());*/

        return CollectionModel.of(files, linkTo(methodOn(NodeControler.class).all()).withSelfRel());
    }

    @GetMapping("/files/{ownerID}")
    CollectionModel<EntityModel<FileNode>> allUser(@PathVariable @NotNull final long ownerID) {
        User u  = userrepository.getById(ownerID);
        List<EntityModel<FileNode>> files = repository.findAllByUser(u).stream()
                .map(assembler::toModel) //
                .collect(Collectors.toList());
                /*.map(file -> EntityModel.of(file,
                        linkTo(methodOn(NodeControler.class).one(file.getHash())).withSelfRel(),
                        linkTo(methodOn(NodeControler.class).all()).withRel("employees")))
                .collect(Collectors.toList());*/

        return CollectionModel.of(files, linkTo(methodOn(NodeControler.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    // Single item

    @GetMapping("/files/id/{id}")
    EntityModel<FileNode> one(@PathVariable @NotNull final Long  id) {

        FileNode file = repository.findById(id).get(); //
                //.orElseThrow(() -> new NodeNotFoundException(id));

        return assembler.toModel(file);
                /*EntityModel.of(node, //
                linkTo(methodOn(NodeControler.class).one(id)).withSelfRel(),
                linkTo(methodOn(NodeControler.class).all()).withRel("nodes"));*/
    }

    @GetMapping("/files/keywords/{keywords}")
    CollectionModel<EntityModel<FileNode>> findBykeywords(@PathVariable @NotNull final String[] keywords) {

        List<FileNode> file = repository.findBykeywords(keywords); //
        //.orElseThrow(() -> new NodeNotFoundException(id));

        return assembler.toModels(file);
                /*EntityModel.of(node, //
                linkTo(methodOn(NodeControler.class).one(id)).withSelfRel(),
                linkTo(methodOn(NodeControler.class).all()).withRel("nodes"));*/
    }

    @GetMapping("/files/description/{description}")
    CollectionModel<EntityModel<FileNode>> findBydescription(@PathVariable @NotNull final String  description) {

        List<FileNode> file = repository.findBydescriptionContaining(description); //
        //.orElseThrow(() -> new NodeNotFoundException(id));


        return assembler.toModels(file);
                /*EntityModel.of(node, //
                linkTo(methodOn(NodeControler.class).one(id)).withSelfRel(),
                linkTo(methodOn(NodeControler.class).all()).withRel("nodes"));*/
    }

/*
    @GetMapping("/files/description/{id}")
    EntityModel<FileNode> oneDesc(String id) {

        FileNode file = repository.findByDescription(id).stream().findAny() //
                .orElseThrow(() -> new NodeNotFoundExceptionDesc(id));

        return assembler.toModel(file);
                //EntityModel.of(node, //
                //linkTo(methodOn(NodeControler.class).one(id)).withSelfRel(),
                //linkTo(methodOn(NodeControler.class).all()).withRel("nodes"));
    }
*/

    @PostMapping("/files/{user_id}")
    FileNode newFile(@RequestBody FileNode newFile,@PathVariable Long user_id) {
        User user = userrepository.findById(user_id) //
                .orElseThrow(() -> new UserNotFoundException(user_id));

        user.addFile(newFile);
        newFile.setUser(user);

        return repository.save(newFile);
    }


    @PutMapping("/files/{id}")
    FileNode replaceFile(@RequestBody FileNode newFile, @PathVariable String id) throws Exception { // iduserActual.idfileAborrar
        String[] idString=id.split(",");
        Long[] lo= new Long[3];
        for(int i=0;i<idString.length;i++){
            lo[i]= Long.valueOf(idString[i]);
        }
        if(repository.getById(lo[1]).getUser().getId().equals(userrepository.getById(lo[0]).getId())){
            return repository.findById(lo[1])
                    .map(file -> {
                        file.setTitle(newFile.getTitle());
                        file.setDescription(newFile.getDescription());
                        file.setKeywords(newFile.getKeywords());
                        return repository.save(file);
                    })
                    .orElseGet(() -> {
                        newFile.setHash(lo[1]);
                        return repository.save(newFile);
                    });
        }else{
            System.out.println("not the owner");
            return null;
        }

    }

    @DeleteMapping("/files/{id}")
    void deleteFile(@PathVariable String id) throws Exception { // iduserActual.idfileAborrar
        String[] idString=id.split(",");
        Long[] lo= new Long[3];
        for(int i=0;i<idString.length;i++){
            lo[i]= Long.valueOf(idString[i]);
        }
        if(repository.getById(lo[1]).getUser().getId().equals(userrepository.getById(lo[0]).getId())){
            repository.deleteById(lo[1]);
        }else{
            System.out.println("not the owner");
        }
    }
}