package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class UserTest {

    private Board board;
    private User user1;
    private User user2;
    private User user3;
    private Question question1;
    private Question question2;
    private Question question3;
    private Answer answer1;
    private Answer answer2;
    private Answer answer3;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        board = new Board("history");

        user1 = new User(board, "cardin238");
        user2 = new User(board, "annyPatino");
        user3 = new User(board, "Gabby849s");

        question1 = user1.askQuestion( "What happens on July 4th?");
        question2 = user2.askQuestion("Who is Barack Obama?");
        question3 = user3.askQuestion("What does diwali celebrate?");

        answer1 = user2.answerQuestion(question1, "USA independence day.");
        answer2 = user3.answerQuestion(question2,"44th president of USA");
        answer3 = user1.answerQuestion(question3, "Hindu festival of lights");
    }

    @Test
    public void userReputationIncreasesByFiveWhenQuestionUpvoted() throws Exception {

        int originalReputation = user1.getReputation();
        user2.upVote(question1);

        assertEquals(originalReputation + 5, user1.getReputation());

    }

    @Test
    public void userReputationDecreasesByOneWhenQuestionDownvoted() throws Exception {
        int originalReputation = user1.getReputation();
        user2.downVote(answer3);

        assertEquals(originalReputation - 1, user1.getReputation());
    }

    @Test
    public void userReputationCalculatesEffectivelyWhenAnswerDownvotedAndUpvoted() throws Exception {
        int originalReputation = user1.getReputation();
        user2.upVote(answer3);
        user3.downVote(answer3);

        assertEquals(originalReputation + 9 , user1.getReputation());
    }

    @Test
    public void userReputationCalculatesCorrectlyAnswerQuestionReputationsAndAnswerVerified() throws Exception {
        int originalReputation = user3.getReputation();
        user1.upVote(question3);
        user2.downVote(answer2);
        user2.acceptAnswer(answer2);

        assertEquals(originalReputation + 19 , user3.getReputation());
    }

    @Test
    public void userReputationIncreasesByTenWhenAnswerUpvoted() throws Exception {

        int originalReputation = user3.getReputation();
        user1.upVote(answer2);

        assertEquals(originalReputation + 10, user3.getReputation());
    }

    @Test
    public void userReputationIncreasesByFifteenWhenAnswerVerified() throws Exception {
        int originalReputation = user2.getReputation();
        user1.acceptAnswer(answer1);

        assertEquals(originalReputation + 15, user2.getReputation());
    }

    @Test
    public void upvoteQuestionIsNotAllowedByOriginalAuthor() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");
        user1.upVote(question1);
    }

    @Test
    public void downvoteQuestionIsNotAllowedByPostAuthor() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");
        user1.downVote(question1);
    }

    @Test
    public void upvoteAnswerIsNotAllowedByOriginalAuthor() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");
        user1.upVote(answer3);
    }

    @Test
    public void downvoteAnswerIsNotAllowedByOriginalAuthor() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");
        user1.downVote(answer3);
    }

    @Test
    public void nonOriginalPostAuthorIsNotAllowedToVerifyAnswer() throws Exception {
        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage("Only Gabby849s can accept this answer as it is their question");
        user2.acceptAnswer(answer3);
    }

}