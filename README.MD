# CSG Search Indexer

A robust text file indexing process for CSG search systems, designed to process files and apply configurable business rules.

## Features

- Processes text files and applies customizable rules to index content
- Uses a modular, extensible architecture based on the Strategy and Factory patterns
- Includes comprehensive unit tests
- Provides detailed logging
- Easily extendable for new business rules

## Current Business Rules

1. Count words that start with "M" or "m" (case-insensitive)
2. Return all words longer than 5 characters

## Requirements

- Java 21 or higher
- JUnit 5 for running tests

## Building the Project

The project uses Maven for build management. To build:

```bash
mvn clean package
```

This will compile the code, run tests, and create a JAR file in the `target` directory.

## Running the Application

```bash
java -jar csg-search-indexer-1.0-SNAPSHOT.jar <path-to-file>
```

## Design Patterns

The system implements two major design patterns:

### Strategy Pattern

The `ProcessingRule` interface defines a contract that all business rules must follow. This allows the system to handle different rules in a uniform way while encapsulating the specific algorithm in each rule implementation.

### Factory Pattern

The `RuleFactory` class is responsible for creating rule instances, abstracting away the details of rule creation. This provides several benefits:
- Centralized rule creation logic
- Simplified client code
- Easier rule parameter validation
- Single point for future rule creation modifications

## Adding New Business Rules

To add a new rule to the system:

1. Create a new class that implements the `ProcessingRule` interface
2. Add a new rule type to the `RuleFactory.RuleType` enum
3. Implement rule creation in the `RuleFactory.createRule()` method
4. Add a convenience method for your rule in `RuleFactory` if needed

Then use it in the main application:

```java
// Create a new rule using the factory
ProcessingRule myNewRule = RuleFactory.createRule(
    RuleFactory.RuleType.YOUR_NEW_RULE_TYPE, 
    param1, param2);
    
// Add it to the processor
processor.addRule(myNewRule);
```

## Architecture

- `SearchIndexer`: Main application entry point
- `ProcessingRule`: Interface for business rules (Strategy Pattern)
- `RuleFactory`: Creates rule instances (Factory Pattern)
- `FileProcessor`: Handles file operations and applies rules

## Package Structure

```
com.csg.searchindexer
├── business
│   ├── RuleFactory.java                # Factory for creating rules
│   │
│   ├── rules
│       ├── LengthWithFilterRule.java   # Implementation for "Words with length"  rule
│       ├── ProcessingRule.java         # Rule interface
│       ├── StartsWithLetterRule.java   # Implementation for "starts with" rule
│
├── handler
│   ├── FileProcessor.java              # Processing the file
│
├── SearchIndexer.java                  # Main application class