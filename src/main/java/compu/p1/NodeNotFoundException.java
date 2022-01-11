package compu.p1;

class NodeNotFoundException extends RuntimeException {

    NodeNotFoundException(Long id) {
        super("Could not find file " + id);
    }

}