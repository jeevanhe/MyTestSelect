# MyTestSelect
# Note this maven integration is done only to get us the maven command to run selected test 
# example:  mvn test -Dtest=AsyncQueryRunnerTest,QueryRunnerTest$1,QueryRunnerTest$2,QueryRunnerTest$MyBean,QueryRunnerTest,BaseResultSetHandlerTestCase,BaseResultSetHandlerTestCase$ToMapCollectionHandler,ResultSetIteratorTest,ArrayListHandlerTest,BeanHandlerTest$SubTestBean,BeanHandlerTest$SubTestBeanInterface,BeanHandlerTest,BeanListHandlerTest$SubTestBean,BeanListHandlerTest$SubTestBeanInterface,BeanListHandlerTest,BeanMapHandlerTest,KeyedHandlerTest,MapHandlerTest,MapListHandlerTest,ArrayHandlerTest,BasicRowProcessorTest,GenerousBeanProcessorTest$TestBean,GenerousBeanProcessorTest,BeanProcessorTest$MapColumnToPropertiesBean,BeanProcessorTest,BooleanColumnHandlerTest,ByteColumnHandlerTest,DoubleColumnHandlerTest,FloatColumnHandlerTest,IntegerColumnHandlerTest,LongColumnHandlerTest,SQLXMLColumnHandlerTest,ShortColumnHandlerTest,StringColumnHandlerTest,TimestampColumnHandlerTest,ServiceLoaderTest,ColumnHandlerTestBase,TestColumnHandler,DbUtilsTest,OutParameterTest,DatePropertyHandlerTest,StringEnumPropertyHandlerTest,PropertyHandlerTest,TestPropertyHandler,SqlNullCheckedResultSetTest,StringTrimmedResultSetTest,ProxyFactoryTest$1,ProxyFactoryTest,QueryLoaderTest,ColumnListHandlerTest,ScalarHandlerTest,BaseTestCase,MockResultSet,MockResultSetMetaData,StatementConfigurationTest,TestBean,TestBean$Ordinal,TestEnum,SqlNullCheckedResultSetMockBlob,SqlNullCheckedResultSetMockClob,SqlNullCheckedResultSetMockRef,SqlNullUncheckedMockResultSet,
# I have not been able to manipulate the surefire plugin to only run these tests using a custom runner

1. Download the source code using git pull
2. In the MyTestSelect folder on your machine, run "mvn install" to generate the jar required for next steps
3. Copy "MyTestSelect-maven-plugin-1.0-SNAPSHOT.jar" from MyTestSelect/target/ folder to the target-proj-folder
4. Make the following changes to the pom.xml in the target-proj-folder
	<---------Changes to target project's pom.xml------------->
	<-----add the following lines------->
	<plugin>
        <groupId>com.mahesh.testSelect</groupId>
        <artifactId>MyTestSelect-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <executions>
          <execution>
             <id>1-test</id>
            <phase>test</phase>
            <goals>
              <goal>select-tests</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <packname>org.apache.commons.dbutils</packname>
        </configuration>  
      </plugin>
      <-----ensure id 2-test as id under maven surefire plugin------->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        ..
        ..
        ..
        <execution>
            <id>2-test</id>
            <phase>test</phase>
            <goals>
                <goal>test</goal>
            </goals>
        </execution>
        ..
        ..
        ..
      </plugin>