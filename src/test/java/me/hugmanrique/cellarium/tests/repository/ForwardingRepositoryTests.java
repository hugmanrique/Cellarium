package me.hugmanrique.cellarium.tests.repository;

import me.hugmanrique.cellarium.ForwardingRepository;
import me.hugmanrique.cellarium.Repository;
import me.hugmanrique.cellarium.simple.SimpleRepository;

public class ForwardingRepositoryTests extends RepositoryTests {

    @Override
    protected Repository newRepository() {
        return new ForwardingRepository(SimpleRepository.newInstance());
    }
}
