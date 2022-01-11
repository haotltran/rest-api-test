//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gss.workshop.testing.requests;

import gss.workshop.testing.tests.TestBase;
import gss.workshop.testing.utils.RestUtils;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.logging.Logger;
import org.apache.groovy.util.Maps;

public class RequestFactory extends TestBase {
  private static final Logger logger = Logger.getLogger(String.valueOf(RequestFactory.class));
  private static HashMap<String, String> params;

  public RequestFactory() {
  }

  public static Response createBoard(String boardName) {
    logger.info("Creating a new board.");
    params.putAll(RestUtils.addParams(Maps.of("name", boardName)));
    String requestPath = String.format(prop.getProperty("boardCreationPath"), version);
    Response res = RestClient.doPostRequestWithParamsAndNoPayload(requestPath, params);
    logger.info("Finish board creation.");
    return res;
  }

  public static Response createBoard(String boardName, boolean defaultList) {
    logger.info("Creating a new board.");
    params.putAll(RestUtils.addParams(Maps.of("name", boardName, "defaultLists", false)));
    String requestPath = String.format(prop.getProperty("boardCreationPath"), version);
    Response res = RestClient.doPostRequestWithParamsAndNoPayload(requestPath, params);
    logger.info("Finish board creation.");
    return res;
  }

  public static Response getBoardById(String boardId) {
    logger.info("Getting an existing board.");
    String requestPath = String.format(prop.getProperty("boardGettingPath"), version, boardId);
    Response res = RestClient.doGetRequestWithParams(requestPath, params);
    logger.info("Board is returned.");
    return res;
  }

  public static Response deleteBoard(String boardId) {
    logger.info("Deleting an existing board.");
    String requestPath = String.format(prop.getProperty("boardGettingPath"), version, boardId);
    Response res = RestClient.doDeleteRequestWithParams(requestPath, params);
    logger.info("Board is deleted.");
    return res;
  }

  public static Response createList(String boardId, String listName) {
    logger.info("Creating a list on a board.");
    params.putAll(RestUtils.addParams(Maps.of("name", listName, "idBoard", boardId)));
    String requestPath = String.format(prop.getProperty("listCreationPath"), version);
    Response res = RestClient.doPostRequestWithParamsAndNoPayload(requestPath, params);
    logger.info("List is created.");
    return res;
  }

  public static Response createCard(String taskName, String listId) {
    logger.info("Creating a card on a list.");
    params.putAll(RestUtils.addParams(Maps.of("name", taskName, "idList", listId)));
    String requestPath = String.format(prop.getProperty("cardCreationPath"), version);
    Response res = RestClient.doPostRequestWithParamsAndNoPayload(requestPath, params);
    logger.info("Card is created.");
    return res;
  }

  public static Response updateCard(String cardId, String listId) {
    logger.info("Updating a card on a list.");
    params.putAll(RestUtils.addParams(Maps.of("idList", listId)));
    String requestPath = String.format(prop.getProperty("cardModificationPath"), version, cardId);
    Response res = RestClient.doPutRequestWithParamsAndNoPayload(requestPath, params);
    logger.info("Card is updated.");
    return res;
  }

  static {
    params = RestUtils.addParams(Maps.of("key", key, "token", token));
  }
}
