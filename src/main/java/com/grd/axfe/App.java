package com.grd.axfe;

import jtdiff.main.*;
import jtdiff.util.*;

import static spark.Spark.*;
import spark.*;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.io.*;


public class App 
{

  public static void main(String[] args) {
        port(getHerokuAssignedPort());
        Spark.staticFileLocation("/public");

        get("/", (req, res) -> "Hello, Mars!!!");


        post("/compute", (req, res) -> {
          //System.out.println(req.queryParams("input1"));
          //System.out.println(req.queryParams("input2"));

          String input1 = req.queryParams("input1");
          InputStream is = new ByteArrayInputStream(input1.getBytes("UTF-8"));

          InputStreamReader reader = new InputStreamReader(is);
          List<Tree> trees = YAMLToTree.buildTreesFromYamlInput(reader);
          Iterator<Tree> iter = trees.iterator();
          Tree sourceTree = iter.next();
          Tree targetTree = iter.next();
          
          Result result = TreeDiff.computeDiff(sourceTree, targetTree);
          //LOGGER.info("Cost: " + result.cost);
          //LOGGER.info("Difference: " + MappingUtil.produceHumanFriendlyMapping(
           //   result.mapping, sourceTree, targetTree));

          String mappingAsDot = DiffToDot.generateDotFromDiff(
              sourceTree, targetTree, result.mapping());


          //String result = CallGraphListener.run(is);

    //String result = "hello mars";

/*          java.util.Scanner sc1 = new java.util.Scanner(input1);
          sc1.useDelimiter("[;\r\n]+");
          java.util.ArrayList<Integer> inputList = new java.util.ArrayList<>();
          while (sc1.hasNext())
          {
            int value = Integer.parseInt(sc1.next().replaceAll("\\s",""));
            inputList.add(value);
          }
          System.out.println(inputList);


          String input2 = req.queryParams("input2").replaceAll("\\s","");
          int input2AsInt = Integer.parseInt(input2);

          boolean result = true;
*/

          Map map = new HashMap();
          map.put("mapping", mappingAsDot);
          map.put("cost", result.cost());
          return new ModelAndView(map, "compute.mustache");
        }, new MustacheTemplateEngine());


        get("/compute",
            (rq, rs) -> {
              Map map = new HashMap();
              map.put("mapping", "not computed yet!");
              map.put("cost", "not computed yet!");
              return new ModelAndView(map, "compute.mustache");
            },
            new MustacheTemplateEngine());
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}