This is a ReadMe detailing how to use the AI functions of the flashcard program.

ProtoAI - ProtoAI is just a constructor class for the AI to receive prompts. You do not need to touch this unless for whatever reason you want to do something other than the function of the app.

Prompt - Prompt is a class holding 3 methods for calling the AI with a specific set of instructions, based on the main functions the AI will have in the app.
   - flaschcardPrmopt - Has one argument for general prompt input (This can be replaced with an extracted pdf or keywords). Will generate 10 questions as well as their answers.
   - quizprompt - Has one argument for general prompt input (This can be replaced with an extracted pdf or keywords). Will generate 10 questions, but not their answers. 
   - quizResults - Has two arguments for quiz questions and the user's given answers. It will mark them all with a grade of either 1 or 0. (It should be noted it is currently not fully accurate.)

pdfReader - A class holding 2 methods for selecting a file, and one for extracting the text from it.
   - findFilePath - No arguments. Has functionality for opening a file select window and grabbing the file path from it. Will not be called in most scenarios.
   - pdfExtract - No arguments. Has functionality for calling findFilePath and extracting the text from that file.

You will find in the Main class within the app package that some debug test code is already commented in to ensure that the functionality is working soundly.
