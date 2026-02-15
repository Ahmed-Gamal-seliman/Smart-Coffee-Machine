# ☕ Smart Coffee Machine
A modular Android application demonstrating a smart coffee machine interface. Built with a focus on Scalability, Testability, and Unidirectional Data Flow.


# Getting Started
Follow these steps to get your local environment synchronized and the project running.

## 1. Prerequisites
Before you begin, ensure you have the following tools installed:

  - Android Studio.

  - Mockoon Desktop: Required for local API simulation.

## 2. Repository Setup
  - Clone the project to your local machine.

## 3. API Simulation (Mockoon)
  - Open Mockoon Desktop.
  - Go to File > Import > Mockoon's format (JSON).
  - Locate and select the mock-api-config.json file you will find it in the master branch .
  - Once imported, click the Green Play button (Start Server) at the top of the Mockoon interface to activate the local endpoints.

## 4. Running the Application
  - Open the cloned project in Android Studio.
  - Wait for the Gradle sync to complete successfully.
  - Select your target device (Emulator).
  - Click Run app.

## Feature Based Modularization
I implemented a Feature Based Modularization strategy This decouples the business logic from the delivery mechanism (UI), ensuring the code base remains maintainable as the team or feature set grows.


# Why this approach?
## 1.	Scalability & Parallel Development:
  By isolating the Smart Coffee Machine into its own module, new features (e.g., :feature:user profile) can be developed in parallel without causing merge conflicts in a monolithic module.
## 2.	Faster Build Times:
  Gradle can skip recompiling the :core or :feature modules if changes are only made in the :app layer, leveraging incremental builds.
## 3.	Strict Dependency Rule:
  The Core module is completely independent of the UI. This ensures that business logic stays pure and isn't affected by changes in the design or UI framework.


  ## Modules Breakdown
### 1- core module
The foundational layer. It contains pure business logic, base classes, and infrastructure concerns (Networking, Error Handling). It is designed to be agnostic of the UI, making it highly reusable across different platforms or future features.
·	:feature:coffee-machine: A pure logic module containing the Domain (UseCases/Repository interface), Data (Repository implementations/Data Sources), and DI (Dependency Injection modules).
·	:app: The "Entry Point" and Presentation Layer. It handles the UI (Compose), ViewModels, and MVI Contracts. It coordinates the navigation between features.


### 2- core data module | Infrastructure & Network Layer
The Core Data module serves as the architectural backbone for data acquisition, designed with a strict adherence to the Dependency Inversion Principle.

#### Key Architectural Features:
##### 1- Library Agnostic Networking:
By utilizing a custom NetworkProvider interface, the infrastructure is decoupled from specific third party implementations This allows the system to switch between Retrofit, Ktor, or any future networking stack without impacting the feature modules.

##### 2- Base Data Transfer Objects (DTOs):
Implemented a hierarchy of BaseDto classes to enforce the DRY (Don't Repeat Yourself) principle. This ensures consistent data structures for metadata, timestamps, and common response wrappers across the entire application.

##### 3- Centralized Error Handling:
Integrated a robust exception handling strategy that maps raw HTTP/Network exceptions into meaningful Domain Results. This prevents low level leakages into the UI layer.

### 3- core domain module | Business Logic & Abstraction
The Domain layer is the Brain of the feature ensuring the business rules are both stable and testable.

#### Architectural Highlights:

##### 1- Dependency Inversion :
By using interfaces for the Repository and ErrorHandler, the Domain layer defines what it needs, while the Data layer defines how to provide it. This decoupling allows for seamless switching between data sources (Remote vs. Local) or error handling strategies.


##### 2- Standardized BaseUseCase:
Implemented a generic BaseUseCase using the Template Method Pattern This orchestrates the flow of data (Loading, Success, and Failure states) in a unified way, drastically reducing boilerplate and ensuring consistent state management across all use cases.

##### 3- Abstraction over Implementation:

ErrorHandler Interface: Decouples business logic from specific network or database exceptions.

BaseDomain Entities: Established a base for domain models to enforce the DRY (Don't Repeat Yourself) principle and maintain a clean data contract for the UI.

### 4- Features module:
Each feature is designed as a self contained unit that follows Clean Architecture principles  These modules depend on :core to leverage shared infrastructure while maintaining their own internal logic.

#### Module Architecture & Visibility:

  ##### Infrastructure Consumption: 
Feature modules have a strict dependency on the :core module This allows them to inherit base classes and common utilities (e.g., BaseUseCase, NetworkProvider) ensuring that every feature follows the same structural contract and quality standards.

# State Management: Finite State Machine (FSM)
To ensure the Smart Coffee Machine is reliable and safe, I implemented a Finite State Machine using Kotlin Sealed Interfaces. This architectural choice ensures that the machine cannot enter an invalid state.

## The Problem:
In complex systems, using multiple independent flags (e.g., isHeating, isBrewing, isError) leads to invalid states for example, a machine that is accidentally "Brewing" and "Heating" at the same time.

## The Solution: Deterministic State Transitions
Instead of independent flags, I modeled the machine as a single, immutable State Object.

## 1. Exhaustive State Modeling
I defined a CoffeeMachineState hierarchy that represents the only legal modes of operation:

Idle: The machine is waiting for input.

Heat: The water is reaching the required temperature.

Ready: The machine is prepared to brew.

Brew: The coffee extraction process is active.

Error: A system failure has occurred, requiring a reset.

1. handleCoffeeMachineState
  The logic acts as a State Guard. Before any action is executed, the ViewModel validates the incoming Event against the current State:

    Context Aware Events: A Start Brew event is only legal if the current state is Ready.

    Safety Net: If an event arrives at the wrong time (e.g. trying to start brewing while in Idle), the transition logic utilizes an else -> current clause, effectively ignoring the invalid command.

2. Guaranteed Cleanup
    By tying side effects to state transitions, I ensured that resources are managed correctly. For instance, moving from Brew to Idle via a BrewingCanceled event triggers an explicit cancelJob(), ensuring no background processes continue to run in an invalid context.

# System State Transition Diagram:
<img width="1034" height="584" alt="image" src="https://github.com/user-attachments/assets/cf675879-11d8-4c4e-9c85-cdd723e5524e" />

# Resilience & State Persistence
## 1. Handling Configuration Changes (e.g., Screen Rotation)
I utilized State Hoisting to move the machine's state ownership from the UI (Composables) into the ViewModel.

   ### Lifecycle Awareness:
   Since ViewModels are designed to survive configuration changes, the CoffeeMachineState is retained in memory.

  ### User Experience:
   If a user rotates the device while the machine is in the Heat or Brew state, the process continues un interrupted. The UI simply reconnects to the existing stream of state updates.

## 2. Handling System Initiated Process Death
  Beyond standard rotation, the application is resilient against Process Death (when the OS kills the app process to reclaim memory).

  ### SavedStateHandle:
  I integrated SavedStateHandle into the ViewModel to persist the machine's state across process recreation.

  ### Manual Restoration:
  Upon recreation, the ViewModel's setInitialState() retrieves the last known state (e.g., Ready or Brew) from the system bundle, allowing the machine to remember its progress.


