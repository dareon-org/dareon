package org.dareon.dataloader;

import java.util.Arrays;
import org.dareon.domain.CFP;
import org.dareon.domain.Proposal;
import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.repository.CFPRepository;
import org.dareon.repository.ProposalRepository;
import org.dareon.repository.RepoRepository;
import org.dareon.repository.RoleRepository;
import org.dareon.repository.UserRepository;

/**
 * 
 * This Class sets initial users and roles for diifferent repositories of this
 * website
 */
public class InitUsersRepos {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private RepoRepository repoRepository;
	private CFPRepository cfpRepository;
	private ProposalRepository proposalRepository;

	/**
	 * 
	 * @param roleRepository2
	 *            details the roleRepository for defining roles of different users
	 * @param userRepository2
	 *            details the userRepository to define difeerent types of users who
	 *            have access to website
	 * @param repoRepository2
	 *            details the repoRepositories to explain user and roles related to
	 *            particulars repositories thus created in the system
	 * @param cfpRepository2
	 *            details the cfpRepository to explain user and roles related to
	 *            particular cfp
	 * @param proposalRepository2
	 *            details the proposalRepository to explain user and roles related
	 *            to particular proposal.
	 */
	public InitUsersRepos(RoleRepository roleRepository2, UserRepository userRepository2,
			RepoRepository repoRepository2, CFPRepository cfpRepository2, ProposalRepository proposalRepository2) {
		this.roleRepository = roleRepository2;
		this.userRepository = userRepository2;
		this.repoRepository = repoRepository2;
		this.cfpRepository = cfpRepository2;
		this.proposalRepository = proposalRepository2;
	}

	/**
	 * defining first 3 initial users and their roles as system admin, repo owner
	 * and dataowner respectively.
	 */
	public void initUsers() {
		// User(String email, String password, String firstName, String lastName, String
		// institution, Collection<Role> roles)
		final User user1 = new User("admin@dareon.org", "admin", "Systems", "Administrator", "Dareon Org",
				Arrays.asList(roleRepository.findByName("ROLE_SA")));
		userRepository.save(user1);
		final User user2 = new User("repoowner@rmit.edu.au", "repoowner", "Repository", "Owner", "RMIT University",
				Arrays.asList(roleRepository.findByName("ROLE_RO")));
		userRepository.save(user2);
		final User user3 = new User("dataowner@rmit.edu.au", "dataowner", "Data", "Owner", "RMIT University",
				Arrays.asList(roleRepository.findByName("ROLE_DO")));
		userRepository.save(user3);

	}

	/**
	 * setting up details of first few test repositories
	 */
	public void initRepos() {
		// Repo(String title, String institution, String definition, String description,
		// User creator, User owner, Boolean status)
		final Repo repo1 = new Repo("Test Repo 1", "Test Repo Institution 1", "Test Repo Definition 1",
				"Test Repo Description 1", userRepository.findByEmail("admin@dareon.org"),
				userRepository.findByEmail("admin@dareon.org"), true);
		repoRepository.save(repo1);
		final Repo repo2 = new Repo("Test Repo 2", "Test Repo Institution 2", "Test Repo Definition 2",
				"Test Repo Description 2", userRepository.findByEmail("admin@dareon.org"),
				userRepository.findByEmail("repoowner@rmit.edu.au"), true);
		;
		repoRepository.save(repo2);
	}

	/**
	 * setting up details of first few test CFPs
	 */
	public void initCFPs() {
		// CFP(String title, String description, String details, Repo repo,
		// Set<Proposal> proposals)
		final CFP cfp1 = new CFP("Test CFP 1", "Test CFP Description 1", "Test CFP Details 1",
				repoRepository.findById((long) 1));
		cfpRepository.save(cfp1);
		final CFP cfp2 = new CFP("Test CFP 2", "Test CFP Description 2", "Test CFP Details 2",
				repoRepository.findById((long) 2));
		;
		cfpRepository.save(cfp2);
	}

	/**
	 * setting up details of first few test Proposals
	 */
	public void initProposals() {
		// Proposal(String title, String description, String details, CFP cfp)
		final Proposal prop1 = new Proposal("Test Proposal 1", "Test Proposal Description 1", "Test Proposal Details 1",
				cfpRepository.findById((long) 1), userRepository.findByEmail("dataowner@rmit.edu.au"));
		proposalRepository.save(prop1);
		final Proposal prop2 = new Proposal("Test Proposal 2", "Test Proposal Description 2", "Test Proposal Details 2",
				cfpRepository.findById((long) 2), userRepository.findByEmail("dataowner@rmit.edu.au"));
		;
		proposalRepository.save(prop2);
	}

}
