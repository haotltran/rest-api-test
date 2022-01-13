package gss.workshop.testing.tests;

import gss.workshop.testing.pojo.board.BoardCreationRes;
import gss.workshop.testing.pojo.card.CardCreationRes;
import gss.workshop.testing.pojo.card.CardUpdateRes;
import gss.workshop.testing.pojo.list.ListCreationRes;
import gss.workshop.testing.requests.RequestFactory;
import gss.workshop.testing.utils.ConvertUtils;
import gss.workshop.testing.utils.OtherUtils;
import gss.workshop.testing.utils.ValidationUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TrelloTests extends TestBase {

  @Test
  public void trelloWorkflowTest() {
    // 1. Create new board without default list
    String boardName = OtherUtils.randomName();
    Response resBoardCreation = RequestFactory.createBoard(boardName, false);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resBoardCreation, 200);

    // VP. Validate a board is created: Board name and permission level
    BoardCreationRes board =
        ConvertUtils.convertRestResponseToPojo(resBoardCreation, BoardCreationRes.class);
    ValidationUtils.validateStringEqual(boardName, board.getName());
    ValidationUtils.validateStringEqual("private", board.getPrefs().getPermissionLevel());

    // -> Store board Id
    String boardId = board.getId();
    System.out.println(String.format("Board Id of the new Board: %s", boardId));

    // 2. Create a TODO list
    String listName_01 = "ToDo";
    Response resToDoListCreation = RequestFactory.createList(boardId, listName_01);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resToDoListCreation, 200);

    // VP. Validate a list is created: name of list, closed attribute
    ListCreationRes list01 =
            ConvertUtils.convertRestResponseToPojo(resToDoListCreation, ListCreationRes.class);
    ValidationUtils.validateStringEqual(listName_01, list01.getName());
    ValidationUtils.validateFalseValue(list01.getClosed());

    // VP. Validate the list was created inside the board: board Id
    ValidationUtils.validateStringEqual(boardId, list01.getIdBoard());

    // -> Store TODO list Id
    String listId_01 = list01.getId();
    System.out.println(String.format("List Id of the ToDo list: %s", listId_01));

    // 3. Create a DONE list
    String listName_02 = "Done";
    Response resDoneListCreation = RequestFactory.createList(boardId, listName_02);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resDoneListCreation, 200);

    // VP. Validate a list is created: name of list, "closed" property
    ListCreationRes list02 =
            ConvertUtils.convertRestResponseToPojo(resDoneListCreation, ListCreationRes.class);
    ValidationUtils.validateStringEqual(listName_02, list02.getName());
    ValidationUtils.validateFalseValue(list02.getClosed());

    // VP. Validate the list was created inside the board: board Id
    ValidationUtils.validateStringEqual(boardId, list02.getIdBoard());

    // -> Store DONE list Id
    String listId_02 = list02.getId();
    System.out.println(String.format("List Id of the Done list: %s", listId_02));

    // 4. Create a new Card in TODO list
    String cardName = OtherUtils.randomCardName();
    Response resCardCreation = RequestFactory.createCard(cardName, listId_01);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resCardCreation, 200);

    // VP. Validate a card is created: task name, list id, board id
    CardCreationRes card =
            ConvertUtils.convertRestResponseToPojo(resCardCreation, CardCreationRes.class);
    ValidationUtils.validateStringEqual(cardName, card.getName());
    ValidationUtils.validateStringEqual(listId_01, card.getIdList());
    ValidationUtils.validateStringEqual(boardId, card.getIdBoard());

    // VP. Validate the card should have no votes or attachments
    ValidationUtils.validateStringEqual("[]", card.getAttachments().toString());
    ValidationUtils.validateStringEqual(0, card.getBadges().getVotes());

    // 5. Move the card to DONE list
    Response resCardModification = RequestFactory.updateCard(card.getId(), listId_02);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resCardModification, 200);

    // VP. Validate the card should have new list: list id
    CardUpdateRes updatedCard =
            ConvertUtils.convertRestResponseToPojo(resCardModification, CardUpdateRes.class);
    ValidationUtils.validateStringEqual(listId_02, updatedCard.getIdList());

    // VP. Validate the card should preserve properties: name task, board Id, "closed" property
    ValidationUtils.validateStringEqual(cardName, updatedCard.getName());
    ValidationUtils.validateStringEqual(boardId, updatedCard.getIdBoard());
    ValidationUtils.validateFalseValue(updatedCard.getClosed());

    // 6. Delete board
    Response resBoardDeletion = RequestFactory.deleteBoard(boardId);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resBoardDeletion, 200);
  }
}
