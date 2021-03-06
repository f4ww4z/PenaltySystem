package main.dataaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.util.RuntimeTypeAdapterFactory;
import main.models.Masters;
import main.models.PHD;
import main.models.Student;
import main.models.Undergraduate;
import org.apache.commons.io.FileUtils;

// Singleton for data input / output
public class StudentsDA {

    private static final String STUDENTS_FILE = RootDA.ROOT_PATH + "students_ps.json";

    private static StudentsDA instance = null;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private StudentsDA() {
    }

    public static StudentsDA getInstance() {
        if (instance == null) {
            instance = new StudentsDA();
        }
        return instance;
    }

    private Gson getGson() {
        // Save subclasses (polymorphic) locally
        RuntimeTypeAdapterFactory<Student> adapter = RuntimeTypeAdapterFactory
                .of(Student.class, "type")
                .registerSubtype(Undergraduate.class, Undergraduate.class.getName())
                .registerSubtype(Masters.class, Masters.class.getName())
                .registerSubtype(PHD.class, PHD.class.getName());

        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapterFactory(adapter)
                .create();
    }

    /**
     * Save the students list locally - currently, in a text file
     *
     * @param students list of students to save
     */
    public void saveStudents(List<Student> students) {

        // Reset the type
        students.forEach((student) -> {
            if (student instanceof Undergraduate) {
                ((Undergraduate) student).type = Undergraduate.class.getName();
            } else if (student instanceof Masters) {
                ((Masters) student).type = Masters.class.getName();
            } else if (student instanceof PHD) {
                ((PHD) student).type = PHD.class.getName();
            }
        });

        String studentsJson = getGson().toJson(students);

        saveStudentsLocally(studentsJson);
    }

    /**
     * Add new student and save it locally
     *
     * @param student
     */
    public void saveNewStudent(Student student) {
        List<Student> students = getStudents();
        students.add(student);
        saveStudents(students);
    }

    private void saveStudentsLocally(String studentsJson) {
        try {
            FileUtils.writeStringToFile(
                    new File(STUDENTS_FILE), studentsJson, RootDA.UTF8);
        } catch (IOException e) {
            System.out.println(
                    "GetData: Cannot save students locally: " + e.getMessage());
        }
    }

    /**
     * Convert the JSON representation of students to List
     *
     * @return List of Students
     */
    public List<Student> getStudents() {

//        JsonParser parser = new JsonParser();
//        JsonArray stdArray = parser.parse(loadStudentsData()).getAsJsonArray();
//
//        List<Student> students = new ArrayList<>();
//        for (JsonElement jsonElement : stdArray) {
//            Student s = getGson().fromJson(jsonElement, STUDENTS_SERIAL_TYPE.getType());
//            students.add(s);
//        }
        String dataAsJson = loadStudentsData();

        Type studentsListType = new TypeToken<List<Student>>() {
        }.getType();

        List<Student> students = getGson().fromJson(dataAsJson, studentsListType);

        return students;
    }

    /**
     * Load JSON representation of students from a file with already defined
     * path
     *
     * @return List of students in JSON format
     */
    private String loadStudentsData() {
        try {
            return FileUtils.readFileToString(
                    new File(STUDENTS_FILE), RootDA.UTF8);
        } catch (IOException e) {
            System.out.println("GetData: Cannot load students: " + e.getMessage());
        }
        return "";
    }

    public void studentsDemoData() {
        List<Student> myStudents = new ArrayList<>();

        myStudents.add(new Masters("Robert Stevens", "S51321", "99110019231931", "software engineering", "PPIMG", "012313141", "B2-516", 0));
        myStudents.add(new Undergraduate("Tan", "S23412", "99110019231931", "software engineering", "PPIMG", "012313141", "B2-516", 0));
        myStudents.add(new PHD("Alif", "S51123", "99110019231931", "software engineering", "PPIMG", "012313141", "B2-516", 0));
        myStudents.add(new Masters("Syed", "S41321", "99110019231931", "software engineering", "PPIMG", "012313141", "B2-516", 0));
        myStudents.add(new PHD("John", "S11321", "99110019231931", "software engineering", "PPIMG", "012313141", "B2-516", 0));
        myStudents.add(new PHD("Bro Handsom", "S01321", "99110019231931", "software engineering", "PPIMG", "012313141", "B2-516", 0));

        saveStudents(myStudents);
    }
}
