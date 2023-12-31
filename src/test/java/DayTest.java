import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Stream;

@Slf4j
public class DayTest {

    @ParameterizedTest
    @MethodSource("getDays")
    public void testDay(IAdventDay adventDay){
        AInputData inputData = adventDay.getClass().getAnnotation(AInputData.class);

        System.out.println("======= Testing Day: " + inputData.day() + " =======");

        String className = adventDay.getClass().getSimpleName();

        assert Integer.parseInt(className.replace("Day", "")) == inputData.day();
        assert inputData.year() == 2023;

        String[] outputs = readOutputs(inputData.day());

        assert outputs != null;
        System.out.println("Output does exist: ✓");

        TestInput testInput = new TestInput(inputData);
        try {
            if(!outputs[0].equalsIgnoreCase("#nottestable#")) {
                String part1 = adventDay.part1(testInput);
                try {
                    long answer = Long.parseLong(part1);
                    long expected = Long.parseLong(outputs[0]);

                    System.out.println("Part 1: " + answer + " | Expected: " + expected);

                    assert answer == expected;
                } catch (NumberFormatException exception) {
                    System.out.println("Part 1: " + part1 + " | Expected: " + outputs[0]);
                    assert part1.equals(outputs[0]);
                }
                System.out.println("Part-1: ✓");
            }else{
                System.out.println("Part-1: x (could not be tested)");
            }
        }catch (NullPointerException exception){ }


        try{
            if(!outputs[1].equalsIgnoreCase("#nottestable#")){
                String part2 = adventDay.part2(testInput);
                try {
                    long answer = Long.parseLong(part2);
                    long expected = Long.parseLong(outputs[1]);

                    System.out.println("Part 2: " + answer + " | Expected: " + expected);
                    assert answer == expected;
                }catch (NumberFormatException exception){
                    System.out.println("Part 2: " + part2 + " | Expected: " + outputs[1]);
                    assert part2.equals(outputs[1]);
                }
                System.out.println("Part-2: ✓");
            } else {
                System.out.println("Part-2: x (could not be tested)");
            }
        }catch (NullPointerException ignored){ }
    }

    static Stream<IAdventDay> getDays(){
        Reflections reflections = new Reflections("com.ancozockt.advent.days");
        return reflections.getTypesAnnotatedWith(AInputData.class).stream().sorted((o1, o2) -> {
            if(o1.getAnnotation(AInputData.class).day() > o2.getAnnotation(AInputData.class).day()){
                return 1;
            }else if(o1.getAnnotation(AInputData.class).day() < o2.getAnnotation(AInputData.class).day()){
                return -1;
            }else{
                return 0;
            }
        }).map(aClass -> (IAdventDay) createNewInstanceOfClass(aClass));
    }

    private String[] readOutputs(int day){
        ArrayList<String> outputs = new ArrayList<>();

        String line;
        try (BufferedReader reader = readFromFile("output/day" + day + "-output")){
            while ((line = reader.readLine()) != null){
                outputs.add(line);
            }
        }catch (IOException ignored) { }

        return outputs.toArray(new String[]{});
    }

    private BufferedReader readFromFile(String fileName){
        InputStream ioStream = getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }

        return new BufferedReader(new InputStreamReader(ioStream));
    }

    private static <T> T createNewInstanceOfClass(Class<T> someClass) {
        try {
            return someClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

}
