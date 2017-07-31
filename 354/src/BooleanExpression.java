
public class BooleanExpression{
    ComplexAmount leftSide = null;
    ComplexAmount rightSide = null;
    String comparisonOperator = "=";


    /*
    * Supported Comparison Operators:
    *
    * =
    * >
    * <
    * <=
    * >=
    * !=
    *
    * */

    BooleanExpression(String description) throws UnimplementedException{
        //get operator
        if(description.contains("<=")){
            comparisonOperator = "<=";
        }
        else if(description.contains(">=")){
            comparisonOperator = ">=";
        }
        else if(description.contains("!=")){
            comparisonOperator = "!=";
        }
        else if(description.contains("=")){
            comparisonOperator = "=";
        }
        else if(description.contains(">")){
            comparisonOperator = ">";
        }
        else if(description.contains("<")){
            comparisonOperator = "<";
        }

        //split description by operator
        String[] tokens = description.split(comparisonOperator);
        //supports only binary comparison
        if(tokens.length != 2){
            throw new UnimplementedException();
        }

        //parse complex amounts
        leftSide = new ComplexAmount(tokens[0]);
        rightSide = new ComplexAmount(tokens[1]);
    }


    public boolean evaluate(Ability.Player player){
        switch(comparisonOperator){
            case "=":
                return leftSide.evaluate(player) == rightSide.evaluate(player);
            case "<=":
                return leftSide.evaluate(player) <= rightSide.evaluate(player);
            case ">=":
                return leftSide.evaluate(player) >= rightSide.evaluate(player);
            case "!=":
                return leftSide.evaluate(player) != rightSide.evaluate(player);
            case ">":
                return leftSide.evaluate(player) > rightSide.evaluate(player);
            case "<":
                return leftSide.evaluate(player) < rightSide.evaluate(player);
            default:
                return false;
        }
    }

    public String getDescription(){
        return leftSide.getDescription() + comparisonOperator + rightSide.getDescription();
    }

}
