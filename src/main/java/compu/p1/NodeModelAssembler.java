package compu.p1;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class NodeModelAssembler implements RepresentationModelAssembler<FileNode,
        EntityModel<FileNode>> {

    @Override
    public EntityModel<FileNode> toModel(FileNode file) {

        return EntityModel.of(file, //
                linkTo(methodOn(NodeControler.class).one(file.getHash())).withSelfRel(),
                linkTo(methodOn(NodeControler.class).all()).withRel("files"));
    }

    public CollectionModel<EntityModel<FileNode>> toModels(List<FileNode> filelist) {

        List<EntityModel<FileNode>> files = filelist.stream()
                .map(file -> EntityModel.of(file,
                        linkTo(methodOn(NodeControler.class).one(file.getHash())).withSelfRel(),
                        linkTo(methodOn(NodeControler.class).all()).withRel("employees")))
                .collect(Collectors.toList());

        return CollectionModel.of(files, linkTo(methodOn(NodeControler.class).all()).withSelfRel());

    }

}
