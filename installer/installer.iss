#define MyAppName "Example"
#define MyAppVersion "1.0.0"
#define MyAppExeName "example.exe"

[Setup]
SourceDir=..
OutputDir=target

AppName={#MyAppName}
AppVersion={#MyAppVersion}

// No admin privilege installation
PrivilegesRequired=lowest

// Enable selected pages
DisableWelcomePage=no
DisableFinishedPage=no

// Output properties
DefaultDirName={localappdata}\{#MyAppName}
DefaultGroupName={#MyAppName}
OutputBaseFilename={#MyAppName} Desktop Installer

// Compression properties
Compression=lzma2
SolidCompression=yes
LZMANumBlockThreads=2

LicenseFile=LICENSE

[Messages]
WelcomeLabel1=Welcome to the {#MyAppName} {#MyAppVersion} Setup Wizard
WelcomeLabel2=The Setup Wizard will install {#MyAppName} {#MyAppVersion} on your computer. Click Next to continue, or Cancel to exit the Setup Wizard.
ClickNext=

ExitSetupMessage=Are you sure you want to cancel {#MyAppName} {#MyAppVersion} installation?

[Files]
Source: "target\{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{userdesktop}\Example"; Filename: "{app}\example.exe"

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "Launch {#MyAppName}"; Flags: nowait postinstall skipifsilent

[Code]
var
  InterruptedPageActive: Boolean;

// Interrupt page is a reskin of the Welcome page
procedure ShowInterruptedPage;
begin
  InterruptedPageActive := True;

  // Override Welcome page Labels
  WizardForm.WelcomeLabel1.Caption := '{#MyAppName} {#MyAppVersion} Setup Wizard was interrupted';
  WizardForm.WelcomeLabel2.Caption :=
    '{#MyAppName} {#MyAppVersion} setup was interrupted. ' +
    'Your system has not been modified. ' +
    'To install this program at a later time, please run the installation again. ' +
    'Click the Finish button to exit the Setup Wizard.';

  // Hide the buttons that go forward/backward
  WizardForm.BackButton.Visible := False;
  WizardForm.NextButton.Visible := False;

  // Turn the Cancel button into the "Finish" button
  WizardForm.CancelButton.Caption := 'Finish';

  // Move the Cancel button to the Next button's position
  WizardForm.CancelButton.Left := WizardForm.NextButton.Left;

  // Switch to the welcome page layout
  WizardForm.OuterNotebook.ActivePage := WizardForm.WelcomePage;
end;

procedure CancelButtonClick(CurPageID: Integer; var Cancel, Confirm: Boolean);
begin
  // If we are already on the interrupted page, clicking "Finish" (Cancel)
  // should close the app immediately without asking.
  if InterruptedPageActive then
  begin
    Cancel := True; // Allow setup termination
    Confirm := False; // Suppress dialog
    Exit;
  end;

  // First time canceling yes/no dialog
  Confirm := False;
  if MsgBox(
    ExpandConstant('Are you sure you want to cancel {#MyAppName} {#MyAppVersion} installation?'),
    mbConfirmation,
    MB_YESNO
  ) = IDYES then
  begin
    ShowInterruptedPage;
    Cancel := False; // Stop the initial close so we can show the "Interrupted" screen
  end
  else
    Cancel := False; // User decided to stay
end;
