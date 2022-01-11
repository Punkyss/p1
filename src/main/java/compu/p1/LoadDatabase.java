package compu.p1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(NodeRepository repository,UserRepository repository2 ) {

        return args -> {
            User user1 = new User("u1","12345");
            User user2 = new User("u2","12345");
            log.info("Preloading " + repository2.save(user1));
            log.info("Preloading " + repository2.save(user2));
            FileNode fn1 = new FileNode("burglar",new String[] { "first", "file" },"first");
            fn1.setUser(user1);
            user1.addFile(fn1);
            FileNode fn11 = new FileNode("burglar",new String[] { "first", "file","pepe" },"first Element bro you know5");
            fn11.setUser(user1);
            user1.addFile(fn11);
            FileNode fn2 = new FileNode("The thief",new String[] { "second", "file" },"second");
            fn2.setUser(user2);
            user2.addFile(fn2);
            log.info("Preloading " + repository.save(fn1));
            log.info("Preloading " + repository.save(fn11));
            log.info("Preloading " + repository.save(fn2));

        };
    }
}
