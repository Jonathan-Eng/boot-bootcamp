package juice.modules;

import com.google.inject.AbstractModule;

public class StrictExplicitBindingModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().requireExplicitBindings();
    }

}
