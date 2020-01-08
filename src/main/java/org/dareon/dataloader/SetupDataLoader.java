package org.dareon.dataloader;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.dareon.domain.Classification;
import org.dareon.domain.Privilege;
import org.dareon.domain.Role;
import org.dareon.domain.User;
import org.dareon.repository.ClassificationRepository;
import org.dareon.repository.CFPRepository;
import org.dareon.repository.PrivilegeRepository;
import org.dareon.repository.ProposalRepository;
import org.dareon.repository.RepoRepository;
import org.dareon.repository.RoleRepository;
import org.dareon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean alreadySetup = false;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RepoRepository repoRepository;

	@Autowired
	private CFPRepository cfpRepository;

	@Autowired
	private ProposalRepository proposalRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private ClassificationRepository classificationRepository;

	// API

	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		if (alreadySetup) {
			return;
		}

		// == create repo privileges
		final Privilege repoReadPrivilege = createPrivilegeIfNotFound("REPO_READ_PRIVILEGE");
		final Privilege repoCreatePrivilege = createPrivilegeIfNotFound("REPO_CREATE_PRIVILEGE");
		final Privilege repoEditPrivilege = createPrivilegeIfNotFound("REPO_EDIT_PRIVILEGE");
		final Privilege repoDeletePrivilege = createPrivilegeIfNotFound("REPO_DELETE_PRIVILEGE");

		// == create cfp privileges
		final Privilege cfpReadPrivilege = createPrivilegeIfNotFound("CFP_READ_PRIVILEGE");
		final Privilege cfpCreatePrivilege = createPrivilegeIfNotFound("CFP_CREATE_PRIVILEGE");
		final Privilege cfpEditPrivilege = createPrivilegeIfNotFound("CFP_EDIT_PRIVILEGE");
		final Privilege cfpDeletePrivilege = createPrivilegeIfNotFound("CFP_DELETE_PRIVILEGE");

		// == create proposal privileges
		final Privilege proposalReadPrivilege = createPrivilegeIfNotFound("PROPOSAL_READ_PRIVILEGE");
		final Privilege proposalCreatePrivilege = createPrivilegeIfNotFound("PROPOSAL_CREATE_PRIVILEGE");
		final Privilege proposalEditPrivilege = createPrivilegeIfNotFound("PROPOSAL_EDIT_PRIVILEGE");
		final Privilege proposalDeletePrivilege = createPrivilegeIfNotFound("PROPOSAL_DELETE_PRIVILEGE");

		// == create clasification privileges
		final Privilege classificationReadPrivilege = createPrivilegeIfNotFound("CLASSIFICATION_READ_PRIVILEGE");
		final Privilege classificationCreatePrivilege = createPrivilegeIfNotFound("CLASSIFICATION_CREATE_PRIVILEGE");
		final Privilege classificationEditPrivilege = createPrivilegeIfNotFound("CLASSIFICATION_EDIT_PRIVILEGE");
		final Privilege classificationDeletePrivilege = createPrivilegeIfNotFound("CLASSIFICATION_DELETE_PRIVILEGE");

		// == create sysadmin privileges
		final Privilege sysAdminCretePrivilege = createPrivilegeIfNotFound("SYSADMIN_CREATE_PRIVILEGE");
		// final Privilege userReadPrivilege =
		// createPrivilegeIfNotFound("USER_READ_PRIVILEGE");

		// == create initial roles
		final List<Privilege> sdPrivileges = Arrays.asList(sysAdminCretePrivilege);

		final List<Privilege> saPrivileges = Arrays.asList(classificationDeletePrivilege, classificationEditPrivilege,
				classificationCreatePrivilege, classificationReadPrivilege, repoReadPrivilege, repoCreatePrivilege,
				repoEditPrivilege, repoDeletePrivilege, cfpReadPrivilege, cfpCreatePrivilege, cfpEditPrivilege,
				cfpDeletePrivilege, proposalCreatePrivilege, proposalDeletePrivilege, proposalEditPrivilege,
				proposalReadPrivilege);

		final List<Privilege> roPrivileges = Arrays.asList(classificationReadPrivilege, cfpReadPrivilege,
				cfpCreatePrivilege, cfpEditPrivilege, cfpDeletePrivilege, proposalDeletePrivilege,
				proposalReadPrivilege, repoReadPrivilege, repoEditPrivilege);

		final List<Privilege> doPrivileges = Arrays.asList(classificationReadPrivilege, proposalReadPrivilege,
				proposalCreatePrivilege, proposalEditPrivilege, proposalDeletePrivilege, cfpReadPrivilege,
				repoReadPrivilege);

		createRoleIfNotFound("ROLE_SD", sdPrivileges);
		createRoleIfNotFound("ROLE_SA", saPrivileges);
		createRoleIfNotFound("ROLE_RO", roPrivileges);
		createRoleIfNotFound("ROLE_DO", doPrivileges);

		final User user = new User();
		user.setFirstName("System");
		user.setLastName("Deployer");
		user.setPassword("sd");
		user.setEmail("sd@dareon.org");
		user.setInstitution("Dareon");
		user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_SD")));
		userRepository.save(user);

		InitUsersRepos initUsersRepos = new InitUsersRepos(roleRepository, userRepository, repoRepository,
				cfpRepository, proposalRepository);
		initUsersRepos.initUsers();
		initUsersRepos.initRepos();
		initUsersRepos.initCFPs();
		initUsersRepos.initProposals();

		// WARNING!

		Classification for1 = new Classification("01", "MATHEMATICAL SCIENCES");
		Classification for2 = new Classification("0101", "PURE MATHEMATICS");
		Classification for3 = new Classification("0102", "APPLIED MATHEMATICS");
		Classification for4 = new Classification("0103", "NUMERICAL AND COMPUTATIONAL MATHEMATICS");
		Classification for5 = new Classification("0104", "STATISTICS");
		Classification for6 = new Classification("0105", "MATHEMATICAL PHYSICS");
		Classification for7 = new Classification("0199", "OTHER MATHEMATICAL SCIENCES");
		for1.setChildren(Arrays.asList(for1, for2, for3, for4, for5, for6, for7));
		for2.setParent(for1);
		for3.setParent(for1);
		for4.setParent(for1);
		for5.setParent(for1);
		for6.setParent(for1);
		for7.setParent(for1);

		Classification for8 = new Classification("010501", "Algebraic Structures in Mathematical Physics");
		Classification for9 = new Classification("010502", "Integrable Systems (Classical and Quantum)");
		Classification for10 = new Classification("010503",
				"Mathematical Aspects of Classical Mechanics, Quantum Mechanics and Quantum Information Theory");
		Classification for11 = new Classification("010504", "Mathematical Aspects of General Relativity");
		Classification for12 = new Classification("010506",
				"Statistical Mechanics, Physical Combinatorics and Mathematical Aspects of Condensed Matter ");
		Classification for13 = new Classification("010599", "Mathematical Physics not elsewhere classified ");

		for6.setChildren(Arrays.asList(for8, for9, for10, for11, for12, for13));
		for8.setParent(for6);
		for9.setParent(for6);
		for10.setParent(for6);
		for11.setParent(for6);
		for12.setParent(for6);
		for13.setParent(for6);

		Classification for14 = new Classification("02", "PHYSICAL SCIENCES");
		Classification for15 = new Classification("03", "CHEMICAL SCIENCES");
		Classification for16 = new Classification("04", "EARTH SCIENCES");
		Classification for17 = new Classification("05", "ENVIRONMENTAL SCIENCES");
		Classification for18 = new Classification("06", "BIOLOGICAL SCIENCES");
		Classification for19 = new Classification("07", "AGRICULTURAL AND VETERINARY SCIENCES");
		Classification for20 = new Classification("08", "INFORMATION AND COMPUTING SCIENCES");
		Classification for21 = new Classification("09", "ENGINEERING");
		Classification for22 = new Classification("10", "TECHNOLOGY");

		classificationRepository.save(for1);
		classificationRepository.save(for2);
		classificationRepository.save(for3);
		classificationRepository.save(for4);
		classificationRepository.save(for5);
		classificationRepository.save(for6);
		classificationRepository.save(for7);
		classificationRepository.save(for8);
		classificationRepository.save(for9);
		classificationRepository.save(for10);
		classificationRepository.save(for11);
		classificationRepository.save(for12);
		classificationRepository.save(for13);
		classificationRepository.save(for14);
		classificationRepository.save(for15);
		classificationRepository.save(for16);
		classificationRepository.save(for17);
		classificationRepository.save(for18);
		classificationRepository.save(for19);
		classificationRepository.save(for20);
		classificationRepository.save(for21);
		classificationRepository.save(for22);
		//
		// fORRepository.save(for8);
		// fORRepository.save(for9);
		// fORRepository.save(for10);
		// fORRepository.save(for11);
		// fORRepository.save(for12);
		// fORRepository.save(for13);
		// fORRepository.save(for14);
		// fORRepository.save(for15);
		// fORRepository.save(for16);
		// fORRepository.save(for17);
		// fORRepository.save(for18);
		// fORRepository.save(for19);

		//

		alreadySetup = true;
	}

	@Transactional
	private final Privilege createPrivilegeIfNotFound(final String name) {
		Privilege privilege = privilegeRepository.findByName(name);
		if (privilege == null) {
			privilege = new Privilege(name);
			privilegeRepository.save(privilege);
		}
		return privilege;
	}

	@Transactional
	private final Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			role.setPrivileges(privileges);
			roleRepository.save(role);
		}
		return role;
	}

}