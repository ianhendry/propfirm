package com.gracefl.propfirm;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.gracefl.propfirm");

        noClasses()
            .that()
            .resideInAnyPackage("com.gracefl.propfirm.service..")
            .or()
            .resideInAnyPackage("com.gracefl.propfirm.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.gracefl.propfirm.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
