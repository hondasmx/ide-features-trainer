package org.jetbrains.training.commands;

import org.jdom.Element;

/**
 * Created by karashevich on 30/01/15.
 */
public class CommandFactory {

    public static Command buildCommand(Element element){

        Command.CommandType cmdType = null;
        if(element == null) {
            return new NoCommand();
        }
        String tagName = element.getName();

        if (tagName == null) return new NoCommand(); //Potential exception leak here

        for (Command.CommandType ct: Command.CommandType.values()){
            if (tagName.toUpperCase().equals(ct.toString().toUpperCase())) {
                cmdType = ct;
                break;
            }
        }

        if (cmdType == null) return new NoCommand();

        switch (cmdType) {
//            case START:
//                return new StartCommand();
//
//            case TEXT:
//                return new TextCommand();
//
//            case MOVECARET:
//                return new MoveCaretCommand();
//
//            case NOCOMMAND:
//                return new NoCommand();
//
            case TRY:
                return new TryCommand();
//
//            case TRYBLOCK:
//                return new TryBlockCommand();

            case COPYTEXT:
                return new CopyTextCommand();

//            case ACTION:
//                return new ActionCommand();
//
//            case REPLAY:
//                return new ReplayCommand();
//
//            case TYPETEXT:
//                return new TypeTextCommand();
//
//            case TRAVERSECARET:
//                return new TraverseCaretCommand();
//
//            case WAIT:
//                return new WaitCommand();
//
            case WIN:
                return new WinCommand();

            case MOUSEBLOCK:
                return new MouseBlockCommand();

            case MOUSEUNBLOCK:
                return new MouseUnblockCommand();
        }

        return new NoCommand();

    }

}
