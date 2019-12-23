package com.example.fitness_app.util;

public class LevelCalculator {

    public static int getLevelFromExperiencePoints(Long experiencePoints){
        int level = 0;
        int experiencePointNeeded = 100;
        int maxLevel = 60;

        for (int i = 0; i <maxLevel ; i++) {

        }

        return level;
    }

    // TODO create method for calculating level from experience points
    private void calculateLevel(int exp)
    {
        int level = 1;
        int expNeeded = 100;
        int lastExp = 0;
        int maxLevel = 60;

        for (int i = level; i < maxLevel; i++)
        {
            if (exp < expNeeded)
            {
                break;
            }
            lastExp = expNeeded;
            if (i < 30)
            {
                expNeeded = 100 * i + (int) (expNeeded * 1.10);
            }
            else if (i < 50)
            {
                expNeeded = 100 * i + (int) (expNeeded * 1.05);
            }
            else
            {
                expNeeded = 100 * i + (int) (expNeeded * 1.025);
            }
            System.out.println("Exp needed for levelup from: " + (level + 1) + ": " + expNeeded);
            level++;
        }

        /*
        if (level < maxLevel)
        {
            float percentDone = (exp-lastExp)*100.0f/(expNeeded-lastExp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                progressBar.setProgress((int) percentDone, true);
            }
            else
            {
                progressBar.setProgress((int) percentDone);
            }
            levelTextView.setText(String.valueOf(level));
            expProgression.setText((exp-lastExp) + "/" + (expNeeded-lastExp));
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                progressBar.setProgress(100, true);
            }
            else
            {
                progressBar.setProgress(100);
            }
        }
         */
    }
}
