/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sample;

import org.openjdk.jmh.annotations.Benchmark;
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


public class MyBenchmark {

    @Benchmark
    public void testMethod() {
        String reader = "<root xmlns='http://example.org'><describe>Good Company</describe><company xmlns='http://example.org'><employee xmlns='http://example.org'><empleename >Jone</empleename><address xmlns='http://example.org' > <contry>China</contry><city>Beijing</city><area>Haidian</area><street>Zhongguancun</street><code>10098</code></address> </employee><employeecount>34353</employeecount></company><shareprice>8880000</shareprice><createdate>2017-11-08</createdate></root>";
        
        Map<String, Object> properties = new HashMap<>();
        properties.put(JAXBRIContext.BACKUP_WITH_PARENT_NAMESPACE, Boolean.TRUE);
        try {
            JAXBContext c = JAXBContext.newInstance(new Class[] {Root.class, Company.class,Employee.class,Address.class},properties);
            for(int i =0; i <100; i++) {
                Root root = null;
                root = (Root) c.createUnmarshaller().unmarshal(new StringReader(reader));
                Company company = root.company;
                Employee employee = company.employee;
                Address address = employee.address;
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(MyBenchmark.class.getSimpleName())
            .forks(1)
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
    

    @XmlRootElement(name = "company",namespace = "http://example.org")
    static class Company {
        @XmlElement(name="employee",namespace = "http://example.org")
        Employee employee;
        @XmlElement(name="employeecount")
        Integer employeecount;
    }
    
    @XmlRootElement(name = "employee",namespace = "http://example.org")
    static class Employee {
        @XmlElement(name="empleename")
        String empleename;
        @XmlElement(name="address",namespace = "http://example.org")
        Address address;
    }
    
    @XmlRootElement(name = "address",namespace = "http://example.org")
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
