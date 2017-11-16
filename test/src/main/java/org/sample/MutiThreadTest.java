package org.sample;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.sun.xml.bind.api.JAXBRIContext;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MutiThreadTest {
    
    @Benchmark
    @BenchmarkMode(Mode.All)
    public void testMethod() {
        String testxml = "<root xmlns='http://example.org'><describe>Good Company</describe><company xmlns='http://example.org'><employee xmlns='http://example.org'><empleename >Jone</empleename><address xmlns='http://example.org' > <contry>China</contry><city>Beijing</city><area>Haidian</area><street>Zhongguancun</street><code>10098</code></address> </employee><employeecount>34353</employeecount></company><shareprice>8880000</shareprice><createdate>2017-11-08 15:34:23</createdate></root>";
        Map<String, Object> properties = new HashMap<>();
        try {
            JAXBContext c = JAXBContext.newInstance(new Class[] {Root.class});
            Root root = null;
            root = (Root) c.createUnmarshaller().unmarshal(new StringReader(testxml));
            Company company = root.company;
            Employee employee = company.employee;
            Address address = employee.address;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
        .include(MutiThreadTest.class.getSimpleName())
        .warmupIterations(5)
        .measurementIterations(5)
        .threads(Runtime.getRuntime().availableProcessors()*2)
        .forks(1)
        .syncIterations(true)
        .build();
        
        new Runner(opt).run();
    }
    
    @XmlRootElement(name = "root",namespace = "http://example.org")
    static class Root {
        @XmlElement(name="describe")
        String describe;
        @XmlElement(name="company",namespace = "http://example.org")
        Company company;
        @XmlElement(name="shareprice")
        java.math.BigDecimal shareprice;
        @XmlElement(name="createdate")
        java.util.Date createdate;
        
    }
    
    
    @XmlType(namespace = "http://example.org")
    static class Company {
        @XmlElement(name="employee",namespace = "http://example.org")
        Employee employee;
        @XmlElement(name="employeecount")
        Integer employeecount;
    }
    
    @XmlType(namespace = "http://example.org")
    static class Employee {
        @XmlElement(name="empleename")
        String empleename;
        @XmlElement(name="address",namespace = "http://example.org")
        Address address;
    }
    
    @XmlType(namespace = "http://example.org")
    static class Address {
        @XmlElement(name="contry")
        String contry;
        @XmlElement(name="city")
        String city;
        @XmlElement(name="area")
        String area;
        @XmlElement(name="street")
        String street;
        @XmlElement(name="code")
        String code;
    }
    
}
