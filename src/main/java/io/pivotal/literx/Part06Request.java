package io.pivotal.literx;

import io.pivotal.literx.domain.User;
import io.pivotal.literx.repository.ReactiveRepository;
import io.pivotal.literx.repository.ReactiveUserRepository;
import org.assertj.core.api.Assertions;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Learn how to control the demand.
 *
 * @author Sebastien Deleuze
 */
public class Part06Request {

	ReactiveRepository<User> repository = new ReactiveUserRepository();

//========================================================================================

	// TODO Create a StepVerifier that initially requests all values and expect 4 values to be received
	StepVerifier requestAllExpectFour(Flux<User> flux) {
		return 	StepVerifier.create(flux).expectNextCount(4).expectComplete();
	}

//========================================================================================

	// TODO Create a StepVerifier that initially requests 1 value and expects User.SKYLER then requests another value and expects User.JESSE then stops verifying by cancelling the source
	StepVerifier requestOneExpectSkylerThenRequestOneExpectJesse(Flux<User> flux) {
		return 
			StepVerifier.create(flux).assertNext(s -> Assertions.assertThat(s.getFirstname()).isEqualToIgnoringCase("SKYLER")).assertNext(s2 -> Assertions.assertThat(s2.getFirstname()).isEqualToIgnoringCase("JESSE")).thenCancel();
	}

//========================================================================================

	// TODO Return a Flux with all users stored in the repository that prints automatically logs for all Reactive Streams signals
	Flux<User> fluxWithLog() {
		return 
			Flux.from(repository.findAll()).log();
	}

//========================================================================================

	// TODO Return a Flux with all users stored in the repository that prints "Starring:" on subscribe, "firstname lastname" for all values and "The end!" on complete
	Flux<User> fluxWithDoOnPrintln() {
		return
			
			Flux.from(repository.findAll()).doOnSubscribe(subscription -> System.out.println("Starring:")).doOnNext(u -> System.out.println(u.getFirstname() + " " + u.getLastname())).doOnComplete(() -> System.out.println("The end!"));
	}

}
