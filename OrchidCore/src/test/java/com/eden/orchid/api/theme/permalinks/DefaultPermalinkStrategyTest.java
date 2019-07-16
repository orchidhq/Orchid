package com.eden.orchid.api.theme.permalinks;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.api.theme.permalinks.pathTypes.DataPropertyPathType;
import com.eden.orchid.api.theme.permalinks.pathTypes.TitlePathType;
import com.eden.orchid.testhelpers.OrchidUnitTest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public final class DefaultPermalinkStrategyTest extends OrchidUnitTest {

    private PermalinkStrategy underTest;
    private Set<PermalinkPathType> pathTypes;

    private OrchidContext context;
    private OrchidReference reference;
    private OrchidPage page;

    @BeforeEach
    public void setUp() {
        pathTypes = new HashSet<>();
        pathTypes.add(new TestPathType(100, "one", "two"));
        pathTypes.add(new TestPathType(150, "two", "four"));
        pathTypes.add(new TestPathType(200, "four", "eight"));

        underTest = new DefaultPermalinkStrategy(pathTypes);

        context = mock(OrchidContext.class);

        reference = new OrchidReference(context, "");
        page = mock(OrchidPage.class);
        when(page.getReference()).thenReturn(reference);
    }

    @Test
    public void testApplyPermalink() {
        underTest.applyPermalink(page, "/{one}/{two}/three/{four}");
        assertThat(page.getReference().toString(), is(equalTo("two/four/three/eight")));

        underTest.applyPermalink(page, "/:one/:two/three/:four");
        assertThat(page.getReference().toString(), is(equalTo("two/four/three/eight")));

        underTest.applyPermalink(page, "/:one/{two}/three/:four");
        assertThat(page.getReference().toString(), is(equalTo("two/four/three/eight")));

        underTest.applyPermalink(page, "/{one}-{two}-three-{four}");
        assertThat(page.getReference().toString(), is(equalTo("two-four-three-eight")));
    }

    @Test
    public void testInvalidPermalinkKey() {
        assertThrows(IllegalArgumentException.class , () -> underTest.applyPermalink(page, "/{one}/{two}/{three}/{four}"));
    }

    @Test
    public void testEmptyPermalinkColons() {
        assertThrows(IllegalArgumentException.class , () -> underTest.applyPermalink(page, "/:one/:two/:/four"));
    }

    @Test
    public void testEmptyPermalinkGroups() {
        assertThrows(IllegalArgumentException.class , () -> underTest.applyPermalink(page, "/{one}/{two}/{}/{four}"));
    }

    @Test
    public void testDataPropertyPathType() {
        StringConverter converter = new StringConverter(new HashSet<>());

        JSONObject jsonData = new JSONObject();
        jsonData.put("pageValue", 2);

        pathTypes.add(new DataPropertyPathType(converter));
        when(page.query(anyString())).thenCallRealMethod();
        when(page.get(anyString())).thenCallRealMethod();
        when(page.getMap()).thenReturn(jsonData.toMap());

        underTest = new DefaultPermalinkStrategy(pathTypes);

        underTest.applyPermalink(page, "/{pageValue}/{two}/three/{four}");
        assertThat(page.getReference().toString(), is(equalTo("2/four/three/eight")));
    }

    @Test
    public void testDataPropertyPathTypeQuery() {
        StringConverter converter = new StringConverter(new HashSet<>());

        JSONObject jsonData = new JSONObject();
        jsonData.put("pageObj", new JSONObject());
        jsonData.getJSONObject("pageObj").put("pageValue", 2);

        pathTypes.add(new DataPropertyPathType(converter));
        when(page.query(anyString())).thenCallRealMethod();
        when(page.get(anyString())).thenCallRealMethod();
        when(page.getMap()).thenReturn(jsonData.toMap());

        underTest = new DefaultPermalinkStrategy(pathTypes);

        underTest.applyPermalink(page, "/{pageObj.pageValue}/{two}/three/{four}");
        assertThat(page.getReference().toString(), is(equalTo("2/four/three/eight")));
    }

    @Test
    public void testPageTitlePathType() {
        pathTypes.add(new TitlePathType());
        when(page.getTitle()).thenReturn("2 For Me");

        underTest = new DefaultPermalinkStrategy(pathTypes);

        underTest.applyPermalink(page, "/:title/:two/three/:four");
        assertThat(page.getReference().toString(), is(equalTo("2-for-me/four/three/eight")));
    }

// Test Path Types
//----------------------------------------------------------------------------------------------------------------------

    private static final class TestPathType extends PermalinkPathType {
        private final String key;
        private final String replacement;

        public TestPathType(int priority, String key, String replacement) {
            super(priority);
            this.key = key;
            this.replacement = replacement;
        }

        @Override
        public boolean acceptsKey(OrchidPage page, String key) {
            return this.key.equalsIgnoreCase(key);
        }

        @Override
        public String format(OrchidPage page, String key) {
            return replacement;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            TestPathType that = (TestPathType) o;
            return Objects.equals(key, that.key) &&
                    Objects.equals(replacement, that.replacement);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), key, replacement);
        }
    }

}