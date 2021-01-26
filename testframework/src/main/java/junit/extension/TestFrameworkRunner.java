package junit.extension;

import embedded.container.EmbeddedContainer;
import embedded.container.impl.TomcatEmbeddedContainer;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class TestFrameworkRunner extends BlockJUnit4ClassRunner {

    public static EmbeddedContainer embeddedContainer = new TomcatEmbeddedContainer();

    public TestFrameworkRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement withBeforeClasses(Statement statement) {
        try {
            embeddedContainer.start();
            System.out.println("Started embedded container!"); //TODO: should improve logging
        } catch (Exception e) {
            System.out.println("Problem while starting embedded container. " + e.getLocalizedMessage());
        }

        return super.withBeforeClasses(statement);
    }

    @Override
    public void run(RunNotifier notifier) {
        try {
            super.run(notifier);
        } finally {
            try {
                embeddedContainer.stop();
                System.out.println("Stopped embedded container!");
            } catch (Exception e) {
                System.out.println("Problem while stopping embedded container. " + e.getLocalizedMessage());
            }
        }
    }
}
