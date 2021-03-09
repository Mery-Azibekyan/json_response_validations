package validationTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class validateJsonTests {


    private String response;
    private String response1;

    @BeforeMethod(groups = {"group1"})
    private void beforeGroup1() {
        final String file = "src/test/resources/data.json";
        try {
            response = new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeMethod(groups = {"group2"})
    private void beforeGroup2() {
        final String file = "src/test/resources/nestedJson.json";
        try {

            response1 = new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(groups = {"group1"})
    public void getBookNames() {
        List<String> titles = new ArrayList<String>();
        titles.add("Sayings of the Century");
        titles.add("Moby Dick");
        List<String> bookTitles = from(response)
                .getList("store.book.findAll { it.price < 10 }.title");
//        assertThat(bookTitles,hasItems("Sayings of the Century","Moby Dick"));
        assertThat(bookTitles, equalTo(titles));
    }

    @Test(groups = {"group1"})
    public void getAllAuthorsLength() {
        int sumOfAllAuthorLengths = from(response)
                .getInt("store.book.author*.length().sum()");
        assertThat(sumOfAllAuthorLengths, equalTo(53));

        int sumOfAllAuthorLengths1 = from(response)
                .getInt("store.book.author.collect{it.author.length()}.sum()");

        assertThat(sumOfAllAuthorLengths1, equalTo(53));

    }

    @Test(groups = {"group1"})
    public void getPriceSum() {
        float sumOfPrices = from(response)
                .getFloat("store.book.price.sum()");
        assertThat(sumOfPrices, equalTo(53.92f));
    }


    @Test(groups = {"group1"})
    public void checkCategory() {
        final String category = from(response)
                .getString("store.book.find{it.author=='Nigel Rees'}.category");
        assertThat(category, equalTo("reference"));

    }


    @Test(groups = {"group1"})
    public void checkAllCategories() {
        final List<String> categories = from(response)
                .getList("store.book.category");
        assertThat(categories, hasItems("fiction", "reference"));
    }

    @Test(groups = {"group1"})
    public void getMaxPrice() {
        final float maxPrice = from(response).getFloat("store.book.price.max()");
        assertThat(maxPrice, is(22.99f));
    }

    @Test(groups = {"group2"})
    public void checkTypesById() {
        final String id = "1001";
        final List<String> types = from(response1).getList("batters.batter*.findAll{it.id==\"1001\"}.type");
        assertThat(types.size(),greaterThan(0));
        assertThat(types, everyItem(equalTo("Regular")));
    }

    @Test(groups = {"group2"})
    public void checkToppingsIds() {
        final List<String> types = from(response1).getList("findAll{it.ppu < 0.6}.topping.id.flatten()");
        assertThat(types.size(),greaterThan(0));
        assertThat(types, everyItem(startsWith("5")));
    }

    @Test(groups = {"group2"})
    public void getIdsByBatterType() {
        final List<String> ids = new ArrayList<String>();
        final List<ArrayList> types = from(response1).getList("batters.batter.type");
        for(ArrayList i :types){
            if(i.contains("Chocolate")){
                ids.add(from(response1).getString("id["+types.indexOf(i)+"]"));
            }
        }
        assertThat(ids.size(),greaterThan(0));
        assertThat(ids, everyItem(oneOf("0001","0003")));
    }

    @Test(groups = {"group2"})
    public void getIdsByBatterType1() {
        final List<ArrayList> ids = from(response1).getList("findAll{it.batters.batter.type.find{it==\"Chocolate\"}}.id");
        assertThat(ids.size(),greaterThan(0));
        assertThat(ids, everyItem(oneOf("0001","0003")));
    }

    @Test(groups = {"group2"})
    public void collectDoubleValues() {
        final List<Float> ppu = from(response1).getList("ppu.collect{(it*2)}");
        System.out.println(ppu);

    }

}
