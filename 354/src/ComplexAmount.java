import java.util.ArrayList;

public class ComplexAmount{
    //rode subclass and root for parse tree
    private class PTNode{
        String value = null;

        PTNode leftChild = null;
        PTNode rightChild = null;
        PTNode parent = null;

        PTNode(String value, PTNode parent){
            this.value = value;
            this.parent  = parent;
        }

        private int evaluate(Ability.Player player){
            if(value.startsWith("c")){
                return count(player, value);
            }
            else if (value.matches("\\d+")){
                return Integer.parseInt(value);
            }
            else if(value.equals("*")){
                return leftChild.evaluate(player) * rightChild.evaluate(player);
            }
            else if(value.equals("/")){
                return leftChild.evaluate(player) / rightChild.evaluate(player);
            }
            else if(value.equals("+")){
                return leftChild.evaluate(player) + rightChild.evaluate(player);
            }
            else if(value.equals("-")){
                return leftChild.evaluate(player) - rightChild.evaluate(player);
            }
            else if(value.equals("^")){
                return (int)(Math.pow((double) leftChild.evaluate(player), (double) rightChild.evaluate(player)));
            }
            return 0;
        }
        private String getDescription(){
            if(leftChild == null && rightChild == null){
                return value;
            }
            else{
                return "("+leftChild.getDescription()+value+rightChild.getDescription()+")";
            }
        }
    }
    private PTNode root = null;

    //Constructor & helper methods
    ComplexAmount(String description) throws UnimplementedException{
        String [] tokens = tokenize(description);

        PTNode currentNode = root;
        PTNode currentParent = null;

        int index = 0;
        while(index < tokens.length){
            if(isNaturalNumber(tokens[index]) || tokens[index].startsWith("count")){
                if(currentNode == null){
                    currentNode = new PTNode(tokens[index],currentParent);
                    if(currentParent != null){
                        currentParent.rightChild = currentNode;
                    }
                    else{
                        root = currentNode;
                    }
                }
                else{
                    throw new UnimplementedException();
                }
            }
            else if(isOperator(tokens[index])){
                if(currentNode == null){
                    throw new UnimplementedException();
                }
                else{
                    //handle lowest precedence operators + -
                    if(tokens[index].equals("+") || tokens[index].equals("-")){
                        //make new node with value
                        PTNode tmp = new PTNode(tokens[index], null);

                        //make new node root with old root as left child
                        tmp.leftChild = root;
                        tmp.leftChild.parent = tmp;
                        root = tmp;

                        //update currents
                        currentNode = tmp.rightChild;
                        currentParent = tmp;
                    }
                    //handle higher precedence operators * / ^
                    else if(tokens[index].equals("*") ||  tokens[index].equals("/") || tokens[index].equals("^")){
                        boolean haveFoundLowerPrecedence = false;
                        PTNode rotationTarget = currentNode.parent;
                        while(! haveFoundLowerPrecedence){
                            if(rotationTarget == null){ //case root
                                haveFoundLowerPrecedence = true;
                            }
                            else{
                                if(hasHigherPrecedence(tokens[index], rotationTarget.value)){
                                    haveFoundLowerPrecedence = true;
                                }
                                else{
                                    rotationTarget = rotationTarget.parent;
                                }
                            }
                        }

                        if(rotationTarget == null){
                            //make new node with value
                            PTNode tmp = new PTNode(tokens[index], null);

                            //make new node root with old root as left child
                            tmp.leftChild = root;
                            tmp.leftChild.parent = tmp;
                            root = tmp;

                            //update currents
                            currentNode = tmp.rightChild;
                            currentParent = tmp;
                        }
                        else{
                            //make new node with value and rotation target's parent
                            PTNode tmp = new PTNode(tokens[index], rotationTarget);

                            //move rotation target's right child to tmp left child
                            tmp.leftChild = rotationTarget.rightChild;
                            tmp.leftChild.parent = tmp;

                            //register tmp with parent
                            tmp.parent.rightChild = tmp;

                            //update currents
                            currentNode = tmp.rightChild;
                            currentParent = tmp;

                        }
                    }
                }
            }
            //handle parentheses ( )
            else if(tokens[index].equals("(")){
                //find index of corresponding ')'
                int rightIndex = -1;
                int balance = 1;
                for(int i = index+1; i < tokens.length; i++){
                    if(tokens[i].equals("(")){
                        balance++;
                    }
                    else if(tokens[i].equals(")")){
                        balance--;
                    }
                    if(balance == 0){
                        rightIndex = i;
                        break;
                    }
                }
                //if no matching ) ever found
                if(rightIndex == -1){
                    throw new UnimplementedException();
                }

                //get subset of tokens as string
                String subDescription = "";
                for(int i = index+1; i < rightIndex; i++){
                    subDescription += tokens[i];
                }

                //make the rotation
                ComplexAmount tmp = new ComplexAmount(subDescription);
                currentNode = tmp.root;
                if(currentParent != null){
                    currentParent.rightChild = currentNode;
                }
                else{
                    root = currentNode;
                }
                currentNode.parent = currentParent;

                //push index past the parentheses
                index = rightIndex;
            }
            else{
                throw new UnimplementedException();
            }
            index++;
        }
    }
    private static String[] tokenize(String description) throws UnimplementedException{
        ArrayList<String> tokens = new ArrayList<>();

        int index = 0;
        while(index < description.length()){

            char c = description.charAt(index);

            switch(c){
                case ' ':
                    index++;
                    continue;
                case '*':
                case '/':
                case '+':
                case '-':
                case '(':
                case ')':
                case '^':
                    index++;
                    tokens.add(""+c);
                    break;
                case 'c':
                    String cDesc = "" + c;
                    while(true){
                        index++;
                        if(index < description.length()){
                            char tmp = description.charAt(index);
                            cDesc += tmp;
                            if(tmp == ')'){
//                                if(!description.matches("^count\\(\\.+\\)$")){
//                                    throw new UnimplementedException();
//                                }
                                tokens.add(cDesc);
                                index++;
                                break;
                            }
                        }
                        else{
                            break;
                        }
                    }
                    break;
                default:
                    String num = "" + c;
                    if(isNaturalNumber(num)){
                        while(isNaturalNumber(num)){
                            index++;
                            if(index < description.length()){
                                num += description.charAt(index);
                            }
                            else{
                                break;
                            }
                        }
                        if(! isNaturalNumber(num)){
                            num = num.substring(0, num.length() - 1);
                        }
                        tokens.add(num);
                    }
                    else{
                        index++;
                    }
            }

        }
        String returnArray[] = new String[tokens.size()];
        tokens.toArray(returnArray);
        return returnArray;
    }
    private static boolean isNaturalNumber(String string){
        return string.matches("\\d+");
    }
    private static boolean isDecimalNumber(String string){ return string.matches("-?\\d+(\\.\\d*)?");}
    private static boolean isOperator(String string){
        return string.equals("*") ||
                string.equals("/") ||
                string.equals("+") ||
                string.equals("-") ||
                string.equals("^");
    }
    private static boolean hasHigherPrecedence(String o1, String o2){
        if(o1.equals("+") || o1.equals("-")){
            return false;
        }
        else if(o1.equals("*") || o1.equals("/")){
            return o2.equals("+") || o2.equals("-");
        }
        else if(o1.equals("^")){
            return o2.equals("+") || o2.equals("-") || o2.equals("*") || o2.equals("/");
        }
        else {
            return false;
        }
    }

    //evaluate and helper method
    public int evaluate(Ability.Player player){
        return root.evaluate(player);
    }
    private static int count(Ability.Player player, String description){
        description = description.substring(6,description.length()-1);
        String [] tokens = description.split(":");

        if(tokens.length == 1){
            if(tokens[0].equals("your-hand")){
                if(player == Ability.Player.PLAYER){
                    return GameEngine.player.getHand().size();
                }
                else{
                    return GameEngine.autoPlayer.getHand().size();
                }
            }
            else if(tokens[0].equals("opponent-hand")){
                if(player == Ability.Player.PLAYER){
                    return GameEngine.autoPlayer.getHand().size();
                }
                else{
                    return GameEngine.player.getHand().size();
                }
            }
        }
        else if(tokens.length >= 2){
            boolean countFromSinglePokemon = false;
            PokemonCard singlePokemon = null;

            if(tokens[1].equals("your-active")){
                countFromSinglePokemon = true;
                if(player == Ability.Player.PLAYER){
                    singlePokemon = GameEngine.player.getActivePokemon();
                }
                else{
                    singlePokemon = GameEngine.autoPlayer.getActivePokemon();
                }
            }
            else if(tokens[1].equals("opponent-active")){
                countFromSinglePokemon = true;
                if(player == Ability.Player.PLAYER){
                    singlePokemon = GameEngine.autoPlayer.getActivePokemon();
                }
                else{
                    singlePokemon = GameEngine.player.getActivePokemon();
                }
            }
            else if(tokens[1].equals("opponent-bench")){
                if(player == Ability.Player.PLAYER){
                    return GameEngine.autoPlayer.getBench().size();
                }
                else{
                    return  GameEngine.player.getBench().size();
                }
            }
            else if(tokens[1].equals("your-bench")){
                if(player == Ability.Player.PLAYER){
                    return GameEngine.player.getBench().size();
                }
                else{
                    return  GameEngine.autoPlayer.getBench().size();
                }
            }
            else if(tokens[1].equals("last")){
                //TODO
            }

            if(countFromSinglePokemon){
                if(tokens.length >= 3){
                    if(tokens[2].equals("energy")){
                        if(tokens.length >= 4){
                            String energyType = tokens[3];
                            EnergyCard.Type typeToCount = EnergyCard.typeFromString(tokens[3]);
                            int energyCount = 0;
                            for(EnergyCard e : singlePokemon.getEnergy()){
                                if(e.getType() == typeToCount){
                                    energyCount++;
                                }
                            }
                            return energyCount;
                        }
                        else{
                            return singlePokemon.getEnergy().size();
                        }
                    }
                    else if(tokens[2].equals("damage")){
                        return (singlePokemon.getMaxHP() - singlePokemon.getCurrentHP()) / 10; //10 is the value of 1 damage token
                    }
                }
            }
        }

        return 0;
    }

    public String getDescription(){
        return root.getDescription();
    }

    //for programmer's experimentation
    public static void main(String args[]){
       try{

           ComplexAmount c = new ComplexAmount("(1+2)*(3-4)/2+2^6");
           System.out.println(c.evaluate(Ability.Player.AI));

           System.out.println("abcdef".matches(".*cde.*$"));

           Ability.parseAbilitiesLine("Red Card:deck:target:opponent:destination:deck:count(opponent-hand),shuffle:target:opponent,draw:opponent:4");
       }
       catch(UnimplementedException e){ e.printStackTrace(); }
    }
}
