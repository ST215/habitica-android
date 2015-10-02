package com.magicmicky.habitrpgwrapper.lib.models.tasks;

import com.habitrpg.android.habitica.HabitDatabase;
import com.habitrpg.android.habitica.R;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by viirus on 10/08/15.
 */
@ModelContainer
@Table(databaseName = HabitDatabase.NAME)
public class Task extends BaseModel {
    public static String TYPE_HABIT = "habit";
    public static String TYPE_TODO = "todo";
    public static String TYPE_DAILY = "daily";
    public static String TYPE_REWARD = "reward";

    @Column
    @PrimaryKey
    String id;
    @Column
    public Float priority;

    @Column
    public String text, notes, attribute, type;

    @Column
    public Double value;

    public List<TaskTag> tags;

    @Column
    public Date dateCreated;

    //Habits
    @Column
    public Boolean up, down;


    //todos/dailies
    @Column
    public Boolean completed;

    public List<ChecklistItem> checklist;


    //dailies
    @Column
    public String frequency;

    @Column
    public Integer everyX, streak;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "days_id",
            columnType = Long.class,
            foreignColumnName = "id")})
    public Days repeat;
    //TODO: private String lastCompleted;


    //todos
    @Column
    public String date;


    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }
    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
    /**
     * @return the priority
     */
    public Float getPriority() {
        return priority;
    }
    /**
     * @param i the priority to set
     */
    public void setPriority(Float i) {
        this.priority = i;
    }
    /**
     * @return the text
     */
    public String getText() {
        return text;
    }
    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }
    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }
    /**
     * @param value the value to set
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * To be allowed to set int value without problems
     * @param value the value to set
     */
    public void setValue(double value) {
        this.setValue(Double.valueOf(value));
    }


    /**
     * Returns a string of the type of the Task
     * @return the string of the Item type
     */
    public String getType() {return this.type;}

    public void setType(String type) {this.type = type;}

    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE}, variableName = "tags")
    public List<TaskTag> getTags() {
        if(tags == null) {
            tags = new Select()
                    .from(TaskTag.class)
                    .where(Condition.column("task_id").eq(this.id))
                    .queryList();
        }
        return tags;
    }

    public void setTags(List<TaskTag> tags) {
        for (TaskTag tag : tags) {
            tag.setTask(this);
        }
        this.tags = tags;
    }

    public boolean containsAnyTagId(ArrayList<String> tagIdList){
        getTags();

        for (TaskTag t : tags){
            if(tagIdList.contains(t.getTag().getId())) {
                return true;
            }
        }

        return false;
    }

    public boolean containsAllTagIds(List<String> tagIdList){
        getTags();

        ArrayList<String> allTagIds = new ArrayList<String>();

        for (TaskTag t : tags){
            allTagIds.add(t.getTag().getId());
        }

        return allTagIds.containsAll(tagIdList);
    }

    /**
     * @return whether or not the habit can be "upped"
     */
    public boolean getUp() {
        return up;
    }
    /**
     * Set the Up value
     * @param up
     */
    public void setUp(Boolean up) {
        this.up = up;
    }
    /**
     * @return whether or not the habit can be "down"
     */
    public boolean getDown() {
        return down;
    }
    /**
     * Set the Down value
     * @param down
     */
    public void setDown(Boolean down) {
        this.down = down;
    }


    public boolean getCompleted() {
        return completed;
    }
    /**
     *  Set whether or not the daily is completed
     * @param completed
     */
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }


    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE}, variableName = "checklist")
    public List<ChecklistItem> getChecklist() {
        if(this.checklist == null) {
            this.checklist = new Select()
                    .from(ChecklistItem.class)
                    .where(Condition.column("task_id").eq(this.id))
                    .queryList();
        }
        return this.checklist;
    }

    public void setChecklist(List<ChecklistItem> checklist) {
        for (ChecklistItem checklistItem : checklist) {
            checklistItem.setTask(this);
        }
        this.checklist = checklist;
    }

    public Integer getCompletedChecklistCount() {
        Integer count = 0;
        for (ChecklistItem item : this.getChecklist()) {
            if (item.getCompleted()) {
                count++;
            }
        }
        return count;
    }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public Integer getEveryX() { return everyX; }
    public void setEveryX(Integer everyX) { this.everyX = everyX; }

    /**
     * @return the repeat array.<br/>
     * This array contains 7 values, one for each days, starting from monday.
     */
    public Days getRepeat() {
        return repeat;
    }
    /**
     * @param repeat the repeat array to set
     */
    public void setRepeat(Days repeat) {
        this.repeat = repeat;
    }

	/**
	 * @return the streak
	 */
    public int getStreak() {
        return streak;
    }
    /**
     * @param streak the streak to set
     */
    public void setStreak(Integer streak) {
        this.streak = streak;
    }


    /**
     * @return the due date
     */
    public String getDate() {
        return date;
    }

    /**
     * Set the due date
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the attribute
     */
    public String getAttribute() {
        return attribute;
    }
    /**
     * @param attribute the attribute to set
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }


    @Override
    public void save() {
        super.save();

        if (this.tags != null) {
            for (TaskTag tag : this.tags) {
                tag.setTask(this);
                tag.save();
            }
        }
        if (this.checklist != null) {
            for (ChecklistItem item : this.checklist) {
                item.setTask(this);
                item.save();
            }
        }
    }

    public int getLightTaskColor()
    {
        if (this.value < -20)
            return R.color.worst_100;
        if (this.value < -10)
            return R.color.worse_100;
        if (this.value < -1)
            return R.color.bad_100;
        if (this.value < 5)
            return R.color.neutral_100;
        if (this.value < 10)
            return R.color.better_100;
        return R.color.best_100;
    }

    /**
     * Get the button color resources depending on a certain score
     *
     * @return the color resource id
     */
    public int getMediumTaskColor()
    {
        if (this.value < -20)
            return R.color.worst_50;
        if (this.value < -10)
            return R.color.worse_50;
        if (this.value < -1)
            return R.color.bad_50;
        if (this.value < 5)
            return R.color.neutral_50;
        if (this.value < 10)
            return R.color.better_50;

        return R.color.best_50;
    }

    /**
     * Get the button color resources depending on a certain score
     *
     * @return the color resource id
     */
    public int getDarkTaskColor()
    {
        if (this.value < -20)
            return R.color.worst_10;
        if (this.value < -10)
            return R.color.worse_10;
        if (this.value < -1)
            return R.color.bad_10;
        if (this.value < 5)
            return R.color.neutral_10;
        if (this.value < 10)
            return R.color.better_10;

        return R.color.best_10;
    }

    public Boolean isDue() {
        //TODO: check if daily is due
        return !this.getCompleted();
    }
}
