import argparse
import json
import sys


# Class to store the CVE data
class Issue:
    def __init__(self, id, summary, scorev2, scorev3, vector, status, link):
        self.id = id
        self.summary = summary
        self.scorev2 = scorev2
        self.scorev3 = scorev3
        self.vector = vector
        self.status = status
        self.link = link

    def __eq__(self, other):
        return self.id == other.id

    def __hash__(self):
        return hash(self.id)

    def __str__(self):
        return self.id

    def toTuple(self):
        return (
            self.id,
            self.summary,
            self.scorev2,
            self.scorev3,
            self.vector,
            self.status,
            self.link,
        )


# Class to store the filtered and ready to print CVes
class CVEFilter:
    def __init__(self, name, status):
        self.name = name
        self.status = status
        self.both = []
        self.patched = []
        self.old = []
        self.new = []

    def setVersion(self, version1, version2=0):
        self.version1 = version1
        self.version2 = version2

    def addCVEBoth(self, issue):
        self.both.append(issue)

    def addCVEPatched(self, issue):
        self.patched.append(issue)

    def addCVEOld(self, issue):
        self.old.append(issue)

    def addCVENew(self, issue):
        self.new.append(issue)


# Class to store the Package with the list of Issues
class Package:
    def __init__(self, name, version):
        self.name = name
        self.version = version
        self.cve = []

    def addCVE(self, cve):
        self.cve.append(cve)


# Class to handle CVEs
class Cve:
    def __init__(self):
        self.__markdownFileName = "output.md"
        self.__packages = []
        self.__printIssues = []
        self.__ignored_cves = []
        self.__version = 0
        self.__scoreV2cf = 0
        self.__scoreV3cf = 0

    # def __del__ (self):
    #    self.__cveJsonFile.close()

    def loadCVEfile(self, fileName):
        try:
            self.__cveJsonFile = open(fileName, "r")
            try:
                self.__jsonData = json.load(self.__cveJsonFile)
            except ValueError as e:
                print("Fail on JSON file: " + fileName)
                print(e)
            self.__jsonPackages = self.__jsonData["package"]

        except FileNotFoundError:
            print("File not found or wrong file name! " + fileName)
            exit(1)
        except:
            print("Error to open file!")
            exit(1)

    def setMarkdonFileName(self, name):
        if name:
            self.__markdownFileName = name

    def setIgnoreCVEList(self, listcve):
        self.__ignored_cves = list(listcve)

    def setCVEVersion(self, version=0):
        self.__version = version

    def setScoreV2CutOff(self, score):
        self.__scoreV2cf = score

    def setScoreV3CutOff(self, score):
        self.__scoreV3cf = score

    def getCVEPackages(self):
        return self.__packages

    def getCVEVersion(self):
        return self.__version

    def printCVEs(self):
        print(self.__jsonData["package"])

    def loadCVEData(self):
        for pack in self.__jsonPackages:
            p = Package(pack["name"], pack["version"])
            entry = False
            for id in pack["issue"]:
                if (float(id["scorev2"]) >= self.__scoreV2cf or float(id["scorev3"]) >= self.__scoreV3cf) and id[
                    "status"
                ] != "Ignored":
                    if not (id["id"] in self.__ignored_cves):
                        p.addCVE(
                            Issue(
                                id["id"],
                                id["summary"],
                                id["scorev2"],
                                id["scorev3"],
                                id["vector"],
                                id["status"],
                                id["link"],
                            )
                        )
                        entry = True
            if entry:
                self.__packages.append(p)

    def __creatMarkdownFile(self):
        try:
            self.__markdownFile = open(self.__markdownFileName, "w")
        except:
            print("ERROR to create the Markdown output file")
            exit(1)

    def printUnpatchedListCVEs(self):
        for package in self.__packages:
            print("--------------- PACKAGE -----------------")
            print(package.name)
            print(package.version)
            print("=============== ISSUES ==================")
            for id in package.cve:
                if id.status != "Patched":
                    print(id.id)
                    print(id.scorev2)
                    print(id.scorev3)
                    print(id.status)

    def compareCVes(self, compCVE):
        self.__creatMarkdownFile()
        packagesNew = compCVE.getCVEPackages()
        for packageOld in self.__packages:
            contain = False
            for packageNew in packagesNew:
                if packageOld.name == packageNew.name:
                    # This case if the package has the same version
                    if packageOld.version == packageNew.version:
                        cveOutput = CVEFilter(packageOld.name, "Kept")
                        cveOutput.setVersion(packageOld.version)
                        self.__checkIssues(cveOutput, packageOld.cve, packageNew.cve)
                        self.__printIssues.append(cveOutput)
                        # remove package that has comparated
                        packagesNew.remove(packageNew)
                        contain = True
                        break

                    # This case if the package was upgrated
                    elif packageOld.version < packageNew.version:
                        cveOutput = CVEFilter(packageOld.name, "Updated")
                        cveOutput.setVersion(packageOld.version, packageNew.version)
                        self.__checkIssues(cveOutput, packageOld.cve, packageNew.cve)
                        self.__printIssues.append(cveOutput)
                        # remove package that has comparated
                        packagesNew.remove(packageNew)
                        contain = True
                        break

                    # This case if the package was Downgrade
                    else:
                        cveOutput = CVEFilter(packageOld.name, "Downgraded")
                        cveOutput.setVersion(packageOld.version, packageNew.version)
                        self.__checkIssues(cveOutput, packageOld.cve, packageNew.cve)
                        self.__printIssues.append(cveOutput)
                        # remove package that has comparated
                        packagesNew.remove(packageNew)
                        contain = True
                        break

            # This case is when the package was removed
            if not contain:
                cveOutput = CVEFilter(packageOld.name, "Removed")
                cveOutput.setVersion(packageOld.version)
                self.__checkIssues(cveOutput, packageOld.cve, [])
                self.__printIssues.append(cveOutput)
                # remove package that has comparated
        # For all cases that packages was added
        for newpackage in packagesNew:
            cveOutput = CVEFilter(newpackage.name, "Added")
            cveOutput.setVersion(newpackage.version)
            self.__checkIssues(cveOutput, [], newpackage.cve)
            self.__printIssues.append(cveOutput)
            # remove package that has comparated
        self.__printCVEs(
            self.__printIssues, self.getCVEVersion(), compCVE.getCVEVersion()
        )

    def __checkIssues(self, cveOut, cveListOld, cveListNew):
        cveOut.new = cveListNew.copy()
        if cveListOld and cveListNew:
            for cveold in cveListOld:
                contain = False
                for cvenew in cveOut.new:
                    if cveold.id == cvenew.id:
                        if (cveold.status == cvenew.status) and (
                            cvenew.status == "Unpatched"
                        ):
                            cveOut.addCVEBoth(cveold)
                            cveOut.new.remove(cvenew)
                            contain = True
                            break
                        elif (cveold.status == cvenew.status) and (
                            cvenew.status == "Patched"
                        ):
                            cveOut.new.remove(cvenew)
                            contain = True
                            break
                        elif (cveold.status != cvenew.status) and (
                            cvenew.status == "Patched"
                        ):
                            cveOut.addCVEPatched(cvenew)
                            cveOut.new.remove(cvenew)
                            contain = True
                            break
                        elif (cveold.status != cvenew.status) and (
                            cvenew.status == "Unpatched"
                        ):
                            cveOut.addCVENew(cvenew)
                            cveOut.new.remove(cvenew)
                            contain = True
                            break
                if not contain:
                    cveOut.addCVEOld(cveold)
        elif cveListNew:
            for cveNew in cveListNew:
                if cveNew.status == "Patched":
                    cveOut.new.remove(cveNew)
        else:
            for cveOld in cveListOld:
                if cveOld.status != "Patched":
                    cveOut.addCVEOld(cveOld)

    def __printCVEs(self, cvePrint, v1, v2):
        self.__markdownFile.write(f"# {v1} -> {v2}")
        for cve in cvePrint:
            if cve.old or cve.both or cve.patched or cve.new:
                if cve.status == "Updated" or cve.status == "Downgraded":
                    self.__markdownFile.write("\n")
                    self.__markdownFile.write(
                        "## "
                        + cve.name
                        + ": "
                        + cve.version1
                        + " -> "
                        + cve.version2
                        + " : "
                        + cve.status
                        + "\n"
                    )
                else:
                    self.__markdownFile.write("\n")
                    self.__markdownFile.write(
                        "## "
                        + cve.name
                        + ": "
                        + cve.version1
                        + " : "
                        + cve.status
                        + "\n"
                    )
                self.__printIs(cve)

    def __printIs(self, issue):
        if issue.both:
            if issue.status == "Updated" or issue.status == "Downgraded":
                self.__markdownFile.write("\n### Affect both versions\n")
            else:
                self.__markdownFile.write("\n### Unsolved\n")
            self.__markdownFile.write("| CVE | STATUS | SCORE V2 | SCORE V3 |\n")
            self.__markdownFile.write("|:--|:--:|:--:|:---:|\n")
            s = '<span style="color:red;"> Vulnerable </span>'
            for i in issue.both:
                self.__markdownFile.write(
                    f"|[{i.id}]({i.link})|{s}|{i.scorev2}|{i.scorev3}|\n"
                )

        if issue.patched:
            self.__markdownFile.write("\n### Fixed in new revision\n")
            self.__markdownFile.write("| CVE | STATUS | SCORE V2 | SCORE V3 |\n")
            self.__markdownFile.write("|:--|:--:|:--:|:---:|\n")
            s = '<span style="color:green;"> Patched </span>'
            for i in issue.patched:
                self.__markdownFile.write(
                    f"|[{i.id}]({i.link})|{s}|{i.scorev2}|{i.scorev3}|\n"
                )

        if issue.old:
            if issue.status == "Removed":
                self.__markdownFile.write("\n### Affected the removed version\n")
            else:
                self.__markdownFile.write("\n### Affect the old version\n")
            self.__markdownFile.write("| CVE | STATUS | SCORE V2 | SCORE V3 |\n")
            self.__markdownFile.write("|:--|:--:|:--:|:---:|\n")
            s = '<span style="color:red;"> Vulnerable </span>'
            for i in issue.old:
                self.__markdownFile.write(
                    f"|[{i.id}]({i.link})|{s}|{i.scorev2}|{i.scorev3}|\n"
                )

        if issue.new:
            if issue.status == "Updated" or issue.status == "Downgraded":
                self.__markdownFile.write("\n### New Issues added\n")
            else:
                self.__markdownFile.write("\n### Affect only new revision\n")
            self.__markdownFile.write("| CVE | STATUS | SCORE V2 | SCORE V3 |\n")
            self.__markdownFile.write("|:--|:--:|:--:|:---:|\n")
            s = '<span style="color:red;"> Vulnerable </span>'
            for i in issue.new:
                self.__markdownFile.write(
                    f"|[{i.id}]({i.link})|{s}|{i.scorev2}|{i.scorev3}|\n"
                )
